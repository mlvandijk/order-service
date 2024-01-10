package com.maritvandijk.orderservice;

public class OrderServiceException extends Throwable {
    public OrderServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
