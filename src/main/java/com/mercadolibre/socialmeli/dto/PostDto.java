package com.mercadolibre.socialmeli.dto;

import com.mercadolibre.socialmeli.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {
    private Integer userId;
    private Integer postId;
    private String date;
    private Product product;
    private Integer category;
    private Double price;
}
