package com.maritvandijk.orderservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class CustomerOrder {
    @Id
    private Long orderId;
    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;

    public CustomerOrder(Long id, Customer customer) {
        this.orderId = id;
        this.customer = customer;
    }

    public CustomerOrder() {

    }

    public Long getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "orderId=" + orderId +
                ", customer=" + customer +
                '}';
    }
}
