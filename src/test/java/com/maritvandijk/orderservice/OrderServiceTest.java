package com.maritvandijk.orderservice;

import com.maritvandijk.orderservice.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    OrderService orderService;
    AutoCloseable closeable;

    @Mock
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void shouldRegisterPaidOrderWithoutCancellations() {
        String price = "39.29";
        CustomerOrder defaultOrder = getDefaultOrderWithPrice(price);
        ArgumentCaptor<CustomerOrder> captor = ArgumentCaptor.forClass(CustomerOrder.class);

        orderService.registerOrder(defaultOrder);

        Mockito.verify(orderRepository).save(captor.capture());
        Assertions.assertEquals(0, captor.getValue().getOrderItems().get(0).getCancellations().size());
    }

    @Test
    void shouldRegisterOrderWithFailedPaymentAsCancelled() {
        String price = "20.17";
        String orderItemId = "1";
        String title = "A Great Book";
        int quantity = 1;
        ArgumentCaptor<CustomerOrder> captor = ArgumentCaptor.forClass(CustomerOrder.class);

        OrderItem orderItem = new OrderItem(orderItemId, title, quantity, createPrice(price), "BOOK", Collections.emptyList());
        CustomerOrder orderPaymentFailed = getCustomerOrder(getDefaultAddress(),
                getDefaultCustomer(getDefaultAddress()),
                orderItem,
                getPaymentInformationFailed(createPrice(price)));

        orderService.registerOrder(orderPaymentFailed);

        Mockito.verify(orderRepository).save(captor.capture());
        Assertions.assertEquals(1, captor.getValue().getOrderItems().get(0).getCancellations().size());
    }

    @Test
    void shouldRegisterCancelledOrderAsIs() {
        String price = "20.17";
        String orderItemId = "1";
        String title = "A Great Book";
        int quantity = 1;
        ArgumentCaptor<CustomerOrder> captor = ArgumentCaptor.forClass(CustomerOrder.class);

        Cancellation cancellation = new Cancellation(CancellationReason.PAYMENT_FAILED, quantity);
        OrderItem cancelledItem = new OrderItem(orderItemId, title, quantity, createPrice(price), "BOOK", Collections.singletonList(cancellation));
        CustomerOrder cancelledOrder = getCustomerOrder(getDefaultAddress(),
                getDefaultCustomer(getDefaultAddress()),
                cancelledItem,
                getPaymentInformationPaidWithCreditCard(createPrice(price)));

        orderService.registerOrder(cancelledOrder);

        Mockito.verify(orderRepository).save(captor.capture());
        Assertions.assertEquals(1, captor.getValue().getOrderItems().get(0).getCancellations().size());
    }

    @Test
    void shouldSetInvalidPhoneNumberToNull() {
        String invalidPhoneNumber = "1234";
        String orderId = "00000000";
        Address addressWithInvalidPhoneNumber = new Address("Street name", "1", "1000 AA", "Amsterdam", invalidPhoneNumber);
        Customer customerWithInvalidPhoneNumber = new Customer(2L, "Invalid PhoneNumber", addressWithInvalidPhoneNumber);
        OrderItem item = getDefaultOrderItem(createDefaultPrice());
        PaymentInformation paymentInformation = getPaymentInformationPaidWithCreditCard(createDefaultPrice());
        CustomerOrder orderWithInvalidPhoneNumber = new CustomerOrder(orderId, customerWithInvalidPhoneNumber, addressWithInvalidPhoneNumber, List.of(item), paymentInformation);

        ArgumentCaptor<CustomerOrder> captor = ArgumentCaptor.forClass(CustomerOrder.class);
        Assertions.assertEquals(invalidPhoneNumber, orderWithInvalidPhoneNumber.getCustomer().getBillingAddress().getPhoneNumber());
        orderService.registerOrder(orderWithInvalidPhoneNumber);

        Mockito.verify(orderRepository).save(captor.capture());
        Assertions.assertNull(captor.getValue().getCustomer().getBillingAddress().getPhoneNumber());
    }

    @Test
    void shouldHandlePhoneNumberIsNull() {
        String orderId = "00000000";
        Address addressWithInvalidPhoneNumber = new Address("Street name", "1", "1000 AA", "Amsterdam", null);
        Customer customerWithInvalidPhoneNumber = new Customer(2L, "Invalid PhoneNumber", addressWithInvalidPhoneNumber);
        OrderItem item = getDefaultOrderItem(createDefaultPrice());
        PaymentInformation paymentInformation = getPaymentInformationPaidWithCreditCard(createDefaultPrice());
        CustomerOrder orderWithInvalidPhoneNumber = new CustomerOrder(orderId, customerWithInvalidPhoneNumber, addressWithInvalidPhoneNumber, List.of(item), paymentInformation);

        ArgumentCaptor<CustomerOrder> captor = ArgumentCaptor.forClass(CustomerOrder.class);
        Assertions.assertNull(orderWithInvalidPhoneNumber.getCustomer().getBillingAddress().getPhoneNumber());
        orderService.registerOrder(orderWithInvalidPhoneNumber);

        Mockito.verify(orderRepository).save(captor.capture());
        Assertions.assertNull(captor.getValue().getCustomer().getBillingAddress().getPhoneNumber());
    }

    private static CustomerOrder getDefaultOrderWithPrice(String price) {
        return getCustomerOrder(getDefaultAddress(),
                getDefaultCustomer(getDefaultAddress()),
                getDefaultOrderItem(createPrice(price)),
                getPaymentInformationPaidWithCreditCard(createPrice(price)));
    }

    private static CustomerOrder getCustomerOrder(Address address, Customer customer, OrderItem item, PaymentInformation paymentInformation) {
        return new CustomerOrder("12345678", customer, address, List.of(item), paymentInformation);
    }

    private static PaymentInformation getPaymentInformationFailed(Money price) {
        return new PaymentInformation("IDEAL", price, "12345678", true, PaymentStatus.FAILED);
    }

    private static PaymentInformation getPaymentInformationPaidWithCreditCard(Money price) {
        return new PaymentInformation("CREDIT CARD", price, "12345678", true, PaymentStatus.PAID);
    }

    private static OrderItem getDefaultOrderItem(Money price) {
        return new OrderItem("87654321", "A Great Book", 1, price, "BOOK", Collections.emptyList());
    }

    private static Money createPrice(String price) {
        return new Money(new BigDecimal(price), "EUR");
    }

    private static Money createDefaultPrice() {
        return new Money(new BigDecimal("19.99"), "EUR");
    }

    private static Customer getDefaultCustomer(Address address) {
        return new Customer(1L, "Marit van Dijk", address);
    }

    private static Address getDefaultAddress() {
        return new Address("Street name", "1", "1000 AA", "Amsterdam", null);
    }
}