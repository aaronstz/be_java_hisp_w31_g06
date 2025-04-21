package com.mercadolibre.socialmeli.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromoPostCountDto implements Serializable {
    private Integer user_id;
    private String user_name;
    private Integer promo_products_count;
}
