package com.maritvandijk.orderservice;

import com.maritvandijk.orderservice.model.CustomerOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class OrderService {

    final
    OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<CustomerOrder> getOrders() {
        return (List<CustomerOrder>) orderRepository.findAll();
    }

    public void registerOrder(CustomerOrder order) {
        orderRepository.save(order);
    }
}
