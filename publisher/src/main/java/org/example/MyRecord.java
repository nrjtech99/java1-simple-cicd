package org.example;

import lombok.Builder;

@Builder
public record MyRecord(String name,
                       double price) {
    public MyRecord {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name.substring(2) + '\'' +
                ", price=" + price +
                '}';
    }
}
