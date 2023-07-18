package com.maritvandijk.orderservice.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue
    private Long id; // technical value
    private String orderItemId;
    private String productTitle;
    private Integer numberOfItems;
    @OneToOne(cascade = CascadeType.ALL)
    private Money price;
    private String productType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Cancellation> cancellations;

    public OrderItem(String orderItemId, String productTitle, Integer numberOfItems, Money price, String productType, List<Cancellation> cancellations) {
        this.orderItemId = orderItemId;
        this.productTitle = productTitle;
        this.numberOfItems = numberOfItems;
        this.price = price;
        this.productType = productType;
        this.cancellations = cancellations;
    }

    public OrderItem() {

    }

    public Long getId() {
        return id;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public Money getPrice() {
        return price;
    }

    public String getProductType() {
        return productType;
    }

    public List<Cancellation> getCancellations() {
        return cancellations;
    }

    public void setCancellations(List<Cancellation> cancellations) {
        this.cancellations = cancellations;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderItemId='" + orderItemId + '\'' +
                ", productTitle='" + productTitle + '\'' +
                ", numberOfItems=" + numberOfItems +
                ", price=" + price +
                ", productType='" + productType + '\'' +
                ", cancellations=" + cancellations +
                '}';
    }
}
