package com.maritvandijk.orderservice.model;

import jakarta.persistence.*;

@Entity
public class PaymentInformation {
    @Id
    @GeneratedValue
    private Long id;
    private String paymentMethod;
    @OneToOne(cascade = CascadeType.ALL)
    private Money paymentAmount;
    private String paymentTransactionId;
    private boolean printInvoice;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public PaymentInformation(String paymentMethod, Money paymentAmount, String paymentTransactionId, boolean printInvoice, PaymentStatus paymentStatus) {
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.paymentTransactionId = paymentTransactionId;
        this.printInvoice = printInvoice;
        this.paymentStatus = paymentStatus;
    }

    public PaymentInformation() {

    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Money getPaymentAmount() {
        return paymentAmount;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public boolean isPrintInvoice() {
        return printInvoice;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "PaymentInformation{" +
                "paymentMethod='" + paymentMethod + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", paymentTransactionId='" + paymentTransactionId + '\'' +
                ", printInvoice=" + printInvoice +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
