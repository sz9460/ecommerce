package com.example.payement.proxy;

import com.example.basedomains.dto.Item;
import com.example.basedomains.dto.ItemOrder;
import com.example.payement.model.Order;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Set;

@FeignClient(name="ms-order")
public interface OrderProxy {
    String AUTH_TOKEN = "Authorization";
    @GetMapping(value = "/{IdOrder}/Items")
    @Headers("Content-Type: application/json")
    Set<ItemOrder> getItems(@RequestHeader(AUTH_TOKEN) String bearerToken, @PathVariable("IdOrder")String IdOrder);
}
//@FeignClient(name="localhost:8000")
//public interface OrderProxy {
//    @GetMapping(value = "/api/orders/{IdOrder}/items/")
//    @Headers("Content-Type: application/json")
//    List<ItemOrder> getItems(@PathVariable("IdOrder")Integer IdOrder);
//}

