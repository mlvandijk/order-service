package com.maritvandijk.orderservice;

import com.maritvandijk.orderservice.model.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
class OrderService {

    final
    OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CustomerOrder getOrderByOrderId(String orderId) {
        return orderRepository.findOrderByOrderId(orderId);
    }

    public List<CustomerOrder> getOrders() {
        return (List<CustomerOrder>) orderRepository.findAll();
    }

    public void registerOrder(CustomerOrder order) {
        CustomerOrder updatedOrder = checkOrder(order);
        orderRepository.save(updatedOrder);
    }

    private CustomerOrder checkOrder(CustomerOrder order) {
        return cancelOrderItemsIfPaymentFailed(order);
    }

    private static CustomerOrder cancelOrderItemsIfPaymentFailed(CustomerOrder order) {
        if (order.getPaymentInformation().getPaymentStatus() == PaymentStatus.FAILED) {
            order.getOrderItems().stream().filter(item -> item.getCancellations().isEmpty()).forEach(item -> {
                Cancellation cancellation = new Cancellation(CancellationReason.PAYMENT_FAILED, item.getNumberOfItems());
                item.setCancellations(Collections.singletonList(cancellation));
            });
        }
        return order;
    }
}
