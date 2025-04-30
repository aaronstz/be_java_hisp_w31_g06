package com.mercadolibre.socialmeli.dto;

import com.mercadolibre.socialmeli.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;


@Data
public class CreatePostDto {

    @NotNull(message = "El ID no puede estar vacío.")
    @Positive(message = "El ID debe ser mayor a 0.")
    private Integer userId;

    private LocalDate date;

    @NotNull(message = "El producto no puede estar vacío.")
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
