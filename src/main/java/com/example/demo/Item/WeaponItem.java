package com.example.demo.Item;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class WeaponItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String ItemName;

    private int damage;
}
