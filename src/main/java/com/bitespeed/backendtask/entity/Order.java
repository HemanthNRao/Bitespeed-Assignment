package com.bitespeed.backendtask.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@Entity
@Setter
@Table(name = "ORDERS")
public class Order {
    public Order() {
    }

    public Order(int id, String phoneNumber, String email, int linkerId, String linkPrecedence, Long createdAt, Long updatedAt, Long deletedAt) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.linkerId = linkerId;
        this.linkPrecedence = linkPrecedence;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String phoneNumber;
    String email;
    int linkerId;
    String linkPrecedence;
    Long createdAt;
    Long updatedAt;
    Long deletedAt;
}
