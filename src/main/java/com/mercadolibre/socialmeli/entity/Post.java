package com.mercadolibre.socialmeli.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter @Setter
public class Post {
    @NotNull(message = "El ID no puede estar vacío.")
    @Positive(message = "El ID debe ser mayor a 0.")
    private Integer userId;

    @NotNull(message = "El ID no puede estar vacío.")
    @Positive(message = "El ID debe ser mayor a 0.")
    private Integer postId;

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
