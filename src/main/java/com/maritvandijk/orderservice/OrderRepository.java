package com.maritvandijk.orderservice;

import com.maritvandijk.orderservice.model.CustomerOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface OrderRepository extends CrudRepository<CustomerOrder, Long> {
    CustomerOrder findOrderByOrderId(String orderId);

    List<CustomerOrder> findOrdersByCustomer_customerId(Long customerId);
}
