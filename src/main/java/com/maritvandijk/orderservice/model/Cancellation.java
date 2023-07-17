package com.maritvandijk.orderservice.model;

import jakarta.persistence.*;

@Entity
public class Cancellation {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private CancellationReason cancellationReason;
    private int quantityCancelled;

    public Cancellation(Long id, CancellationReason cancellationReason, int quantityCancelled) {
        this.id = id;
        this.cancellationReason = cancellationReason;
        this.quantityCancelled = quantityCancelled;
    }


    public Cancellation() {

    }

    public Long getId() {
        return id;
    }

    public CancellationReason getCancellationReason() {
        return cancellationReason;
    }

    public int getQuantityCancelled() {
        return quantityCancelled;
    }

    @Override
    public String toString() {
        return "Cancellation{" +
                "id=" + id +
                ", cancellationReason=" + cancellationReason +
                ", quantityCancelled=" + quantityCancelled +
                '}';
    }
}
