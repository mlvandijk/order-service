package com.maritvandijk.orderservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Customer {
    @Id
    private Long customerId;
    private String name;

    public Customer(Long id, String name) {
        this.customerId = id;
        this.name = name;
    }

    public Customer() {

    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                '}';
    }
}
