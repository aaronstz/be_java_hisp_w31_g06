package com.mercadolibre.socialmeli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowDto {

    @NotNull(message = "El ID no puede estar vacío.")
    @Positive(message = "El ID debe ser mayor a 0.")
    private Integer userId;

    @Size(max=15, message = "El nombre no puede tener más de 15 caracteres.")
    @NotNull(message = "El nombre no puede ser vacío.")
    private String userName;
}
