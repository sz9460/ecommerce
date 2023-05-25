package com.example.payement.controller;

import com.example.basedomains.dto.*;
import com.example.payement.entity.Payement;
import com.example.payement.kafka.PayementConsumer;
import com.example.payement.kafka.Producer;
import com.example.payement.kafka.Producerr;
import com.example.payement.proxy.OrderProxy;
import com.example.payement.repos.PayementRepos;
import com.example.payement.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class PaypalController {
    @Autowired
    PaypalService service;
    @Autowired
    Producer producer;

    @Autowired
    Producerr producerr;
    @Autowired
    PayementRepos payementRepos;
    @Autowired
    OrderProxy orderProxy;
    @Autowired
    PayementConsumer payementConsumer;



    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";
//    @GetMapping("/orders/{IdOrder}")
//    public List<ItemOrder> getItems( @PathVariable("IdOrder") Integer IdOrder){
//        return  orderProxy.getItems(IdOrder);
//    }


//    @GetMapping("/orders/{IdOrder}")
//    public String getOrders(@NonNull HttpServletRequest request, @PathVariable("IdOrder") String IdOrder,ProductPayementEvent event) throws InterruptedException {
//
//        String message= payementConsumer.getMessage();
//        for(int i=0;i<=10;i++){
//            System.out.println("hello");
//        }
//        return message;
//    }




    @PostMapping("/pay/{IdOrder}")
    public String payment(@RequestParam("price") Double price,
                          @RequestParam("currency") String currency,
                          @RequestParam("method") String method,
                          @RequestParam("intent") String intent,
                          @RequestParam("description") String description,@NonNull HttpServletRequest request,@PathVariable("IdOrder") String IdOrder ) {


        try {
            Set<ItemOrder> items = orderProxy.getItems(request.getHeader("Authorization"),IdOrder);
//            List<ItemOrder> items = orderProxy.getItems(IdOrder);
            PayementProductEvent payementProductEvent = new PayementProductEvent();
            payementProductEvent.setItems(items);
            producer.sendMessage1(payementProductEvent);
            Payment payment = service.createPayment(price, currency, method,
                    intent, description, "http://localhost:9090/" + CANCEL_URL,
                    "http://localhost:9090/" + SUCCESS_URL);
            Payement payement1 =new Payement();
            payement1.setPayementId(payment.getId());
            payement1.setOrderId(IdOrder);
            payementRepos.save(payement1);
            System.out.println(payment.getId());
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        if(payementConsumer.getMessage().equals("True")){
            try {
                Payment payment = service.executePayment(paymentId, payerId);
                Payement payement1 = payementRepos.findPayementByPayementId(paymentId);
                PayemantEvent payemantEvent =new PayemantEvent();
                payemantEvent.setIdOrder(payement1.getOrderId());
                payemantEvent.setStatus(payment.getState());
                payemantEvent.setMessage("new payement");
                producerr.sendMessage(payemantEvent);
                System.out.println(payment.toJSON());
                if (payment.getState().equals("approved")) {
                    return "success";
                }
            } catch (PayPalRESTException e) {
                System.out.println(e.getMessage());
            }

        }
        else {
            return "failed";
        }

        return "redirect:/";
    }

}
