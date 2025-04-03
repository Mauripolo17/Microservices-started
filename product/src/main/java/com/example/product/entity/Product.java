package com.example.product.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product implements Serializable {

    @Id
    private UUID id;
    private String name;
    private String description;
    private Double price;

    @Serial
    private static final long serialVersionUID =1L;
}
