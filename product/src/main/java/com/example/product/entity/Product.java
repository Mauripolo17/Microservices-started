package com.example.product.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private UUID id;
    private String name;
    private String description;
    private Double price;

}
