package com.mercadolibre.socialmeli.dto;

import lombok.Data;

import java.util.List;

@Data
public class PromoPostDto {
    private Integer userId;
    private String userName;
    private List<PostDto> posts;
}
