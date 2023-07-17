package com.maritvandijk.orderservice.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue
    private Long id; // technicalId
    private String orderId;
    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;
    @OneToOne(cascade = CascadeType.ALL)
    private Address shippingAddress;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;
    @OneToOne(cascade = CascadeType.ALL)
    private PaymentInformation paymentInformation;

    public CustomerOrder(String orderId, Customer customer, Address shippingAddress, List<OrderItem> orderItems, PaymentInformation paymentInformation) {
        this.orderId = orderId;
        this.customer = customer;
        this.shippingAddress = shippingAddress;
        this.orderItems = orderItems;
        this.paymentInformation = paymentInformation;
    }

    public CustomerOrder() {

    }

    public Long getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public PaymentInformation getPaymentInformation() {
        return paymentInformation;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", customer=" + customer +
                ", shippingAddress=" + shippingAddress +
                ", orderItems=" + orderItems +
                ", paymentInformation=" + paymentInformation +
                '}';
    }
}
