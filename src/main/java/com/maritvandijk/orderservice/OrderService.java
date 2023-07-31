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
        CustomerOrder updatedOrder = checkAndUpdateOrder(order);
        orderRepository.save(updatedOrder);
    }

    private CustomerOrder checkAndUpdateOrder(CustomerOrder order) {
        cancelOrderItemsIfPaymentFailed(order);
        clearInvalidPhoneNumber(order);
        return order;
    }

    private static void cancelOrderItemsIfPaymentFailed(CustomerOrder order) {
        if (order.getPaymentInformation().getPaymentStatus() == PaymentStatus.FAILED) {
            order.getOrderItems().stream().filter(item -> item.getCancellations().isEmpty()).forEach(item -> {
                Cancellation cancellation = new Cancellation(CancellationReason.PAYMENT_FAILED, item.getNumberOfItems());
                item.setCancellations(Collections.singletonList(cancellation));
            });
        }
    }

    private void clearInvalidPhoneNumber(CustomerOrder order) {
        boolean isValidPhoneNumber = isValidPhoneNumber(order.getCustomer().getBillingAddress().getPhoneNumber());
        if (!isValidPhoneNumber) {
            Address billingAddress = order.getCustomer().getBillingAddress();
            billingAddress.setPhoneNumber(null);
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String validPhoneNumber = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$";
        if (phoneNumber == null) {
            return true;
        }
        return phoneNumber.matches(validPhoneNumber);
    }
}
