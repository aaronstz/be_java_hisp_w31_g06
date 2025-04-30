package com.mercadolibre.socialmeli.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto implements Serializable {
    private Integer productId;
    private String productName;
    private String type;
    private String brand;
    private String color;
    private String notes;
}
