package com.example.demo.model;

import java.util.Objects;

public record Product(String id, String name, int units, double perUnitPrice) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Double.compare(perUnitPrice, product.perUnitPrice) == 0 && Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, perUnitPrice);
    }
}