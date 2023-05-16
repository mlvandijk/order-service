package com.maritvandijk.orderservice;

import com.maritvandijk.orderservice.model.Customer;
import com.maritvandijk.orderservice.model.CustomerOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        Customer customer = new Customer(11223344L, "Customer Name");
        CustomerOrder order = new CustomerOrder(12345678L, customer);
        repository.save(order);
    }
}
