package com.maritvandijk.orderservice;

import com.maritvandijk.orderservice.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
class OrderService {

    final
    OrderRepository orderRepository;

    private static final Log log = LogFactory.getLog(OrderService.class);

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
        log.info("Registering order " + order.getOrderId());
        CustomerOrder updatedOrder = checkAndUpdateOrder(order);
        orderRepository.save(updatedOrder);
    }

    private CustomerOrder checkAndUpdateOrder(CustomerOrder order) {
        order = cancelOrderItemsIfPaymentFailed(order);
        order = clearInvalidPhoneNumber(order);
        return order;
    }

    private static CustomerOrder cancelOrderItemsIfPaymentFailed(CustomerOrder order) {
        if (order.getPaymentInformation().getPaymentStatus() == PaymentStatus.FAILED) {
            log.info("Payment failed for order" + order.getOrderId() + ". Cancelling order items.");
            order.getOrderItems().stream().filter(item -> item.getCancellations().isEmpty()).forEach(item -> {
                Cancellation cancellation = new Cancellation(CancellationReason.PAYMENT_FAILED, item.getNumberOfItems());
                item.setCancellations(Collections.singletonList(cancellation));
            });
        }
        return order;
    }

    private CustomerOrder clearInvalidPhoneNumber(CustomerOrder order) {
        // Check that the number we have is a valid number
        // If the number is not valid, remove the invalid number
        boolean isValidPhoneNumber = isValidPhoneNumber(order.getCustomer().getBillingAddress().getPhoneNumber());
        if (!isValidPhoneNumber) {
            log.info("Removing invalid phone number" + order.getCustomer().getBillingAddress().getPhoneNumber());
            Address billingAddress = order.getCustomer().getBillingAddress();
            billingAddress.setPhoneNumber(null);
        }
        return order;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String validPhoneNumber = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$";
        if (phoneNumber == null) {
            return true;
        }
        return phoneNumber.matches(validPhoneNumber);
    }
}
