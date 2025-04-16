package com.mercadolibre.socialmeli.dto;

import com.mercadolibre.socialmeli.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {
    private Integer userId;
    private Integer postId;
    private LocalDate date;
    private Product product;
    private Integer category;
    private Double precio;
}
