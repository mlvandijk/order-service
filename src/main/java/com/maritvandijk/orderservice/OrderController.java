package com.maritvandijk.orderservice;

import com.maritvandijk.orderservice.model.CustomerOrder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/orders/{orderId}")
    CustomerOrder getOrderByOrderId(@PathVariable String orderId) {
        return orderService.getOrderByOrderId(orderId);
    }

    @PostMapping("/orders")
    void registerOrder(@RequestBody CustomerOrder order) {
        orderService.registerOrder(order);
    }
}
