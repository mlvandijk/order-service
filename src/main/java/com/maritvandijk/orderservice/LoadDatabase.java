package com.maritvandijk.orderservice;

import com.maritvandijk.orderservice.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Configuration
class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    public CommandLineRunner run(OrderRepository repository) {
        return (args -> {
            insertOrders(repository);
            repository.findAll().forEach(order -> log.info("Preloaded " + order));
        });
    }

    private void insertOrders(OrderRepository repository) {
        Address address = new Address("Street name", "1", "1000 AA", "Amsterdam", null);
        Customer customer = new Customer(1L, "Customer Name", address);
        Money price = new Money(new BigDecimal("11.99"), "EUR");
        OrderItem item = new OrderItem("11111111", "A Great Book", 1, price, "BOOK", Collections.emptyList());
        PaymentInformation paymentInformation = new PaymentInformation("CREDIT CARD", price, "1212121212", true, PaymentStatus.PAID);
        CustomerOrder order = new CustomerOrder("ABC12345678", customer, address, List.of(item), paymentInformation);
        repository.save(order);
    }
}
