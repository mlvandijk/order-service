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

    public void registerOrder(CustomerOrder order) {
        log.info("Registering order " + order.getOrderId());
        CustomerOrder updatedOrder = checkAndUpdateOrder(order);
        orderRepository.save(updatedOrder);
    }

    public List<CustomerOrder> getOrdersForCustomer(Long customerId) {
        return orderRepository.findOrdersByCustomer_customerId(customerId);
    }

    private CustomerOrder checkAndUpdateOrder(CustomerOrder order) {
        cancelOrderItemsIfPaymentFailed(order);
        clearInvalidPhoneNumber(order);
        return order;
    }

    private static void cancelOrderItemsIfPaymentFailed(CustomerOrder order) {
        if (order.getPaymentInformation().getPaymentStatus() == PaymentStatus.FAILED) {
            log.info("Payment failed for order" + order.getOrderId() + ". Cancelling order items.");
            order.getOrderItems().stream().filter(item -> item.getCancellations().isEmpty()).forEach(item -> {
                Cancellation cancellation = new Cancellation(CancellationReason.PAYMENT_FAILED, item.getNumberOfItems());
                item.setCancellations(Collections.singletonList(cancellation));
            });
        }
    }

    private void clearInvalidPhoneNumber(CustomerOrder order) {
        Address billingAddress = order.getCustomer().getBillingAddress();
        if (billingAddress != null && billingAddress.getPhoneNumber() != null) {
            boolean isValidPhoneNumber = isValidPhoneNumber(billingAddress.getPhoneNumber());
            if (!isValidPhoneNumber) {
                log.info("Removing invalid phone number" + billingAddress.getPhoneNumber());
                billingAddress.setPhoneNumber(null);
            }
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
