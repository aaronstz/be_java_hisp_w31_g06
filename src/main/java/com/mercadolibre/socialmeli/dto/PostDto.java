package com.mercadolibre.socialmeli.dto;

import com.mercadolibre.socialmeli.entity.Product;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto implements Serializable {

    @NotNull(message = "El ID no puede estar vacío.")
    @Positive(message = "El ID debe ser mayor a 0.")
    private Integer userId;

    @NotNull(message = "El ID no puede estar vacío.")
    @Positive(message = "El ID debe ser mayor a 0.")
    private Integer postId;

    @PastOrPresent(message = "La fecha no puede ser futura a la fecha actual.")
    @NotNull(message = "El campo no puede estar vacío.")
    private String date;

    private Product product;

    @NotNull(message = "El campo no puede estar vacío.")
    @Positive(message = "El campo debe ser mayor a 0.")
    private Integer category;

    @NotNull(message = "El campo no puede estar vacío.")
    @Positive(message = "El campo debe ser mayor a 0.")
    @Max(value = 10000000, message = "El precio máximo no puede superar los $10.000.000")
    private Double price;

    private Boolean hasPromo;

    @PositiveOrZero(message = "El campo debe ser mayor a 0.")
    private Double discount;
}
