package com.mercadolibre.socialmeli.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowerCountDto implements Serializable {

    @NotNull(message = "El ID no puede estar vacío.")
    @Positive(message = "El ID debe ser mayor a 0.")
    private Integer userId;

    @Size(max=15, message = "El nombre no puede tener más de 15 caracteres.")
    @NotNull(message = "El nombre no puede ser vacío.")
    private String userName;

    @PositiveOrZero
    private Integer followersCount;
}
