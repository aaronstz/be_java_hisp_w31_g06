package com.mercadolibre.socialmeli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mercadolibre.socialmeli.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto implements Serializable {
    private Integer userId;
    private Integer postId;
    private String date;
    private Product product;
    private Integer category;
    private Double price;
    private Boolean hasPromo;
    private Double discount;
}
