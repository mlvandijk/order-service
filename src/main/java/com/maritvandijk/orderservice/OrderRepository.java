package com.maritvandijk.orderservice;

import com.maritvandijk.orderservice.model.CustomerOrder;
import org.springframework.data.repository.CrudRepository;

interface OrderRepository extends CrudRepository<CustomerOrder, Long> {
}
