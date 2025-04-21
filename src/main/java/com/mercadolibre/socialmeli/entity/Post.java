package com.mercadolibre.socialmeli.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {
    private Integer userId;
    private Integer postId;
    private String date;
    private Product product;
    private Integer category;
    private Double price;
    private Boolean hasPromo;
}
