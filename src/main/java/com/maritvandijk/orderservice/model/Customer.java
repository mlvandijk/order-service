package com.maritvandijk.orderservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Customer {
    @Id
    private Long customerId;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    private Address billingAddress;

    public Customer(Long customerId, String name, Address billingAddress) {
        this.customerId = customerId;
        this.name = name;
        this.billingAddress = billingAddress;
    }

    public Customer() {

    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", billingAddress=" + billingAddress +
                '}';
    }
}
