package com.maritvandijk.orderservice;

import com.maritvandijk.orderservice.model.CustomerOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class OrderController {

    final
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    List<CustomerOrder> getOrders() {
        return orderService.getOrders();
    }

    @PostMapping("/orders")
    void registerOrder(@RequestBody CustomerOrder order) {
        orderService.registerOrder(order);
    }
}
