package com.mercadolibre.socialmeli.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @NotNull(message = "El ID no puede estar vacío.")
    @Positive(message = "El ID debe ser mayor a 0.")
    private Integer productId;

    @Size(max=40, message = "El nombre no puede tener más de 40 caracteres.")
    @NotNull(message = "El nombre no puede ser vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,-]+$", message = "El campo solo permite letras, números, espacios, puntos, comas y guiones.")
    private String productName;

    @Size(max=15, message = "El campo no puede tener más de 15 caracteres.")
    @NotNull(message = "El campo no puede ser vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El campo solo permite letras, números, espacios, puntos, comas y guiones.")
    private String type;

    @Size(max=25, message = "El campo no puede tener más de 25 caracteres.")
    @NotNull(message = "El campo no puede ser vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El campo solo permite letras, números, espacios, puntos, comas y guiones.")
    private String brand;

    @Size(max=15, message = "El campo no puede tener más de 15 caracteres.")
    @NotNull(message = "El campo no puede ser vacío.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El campo solo permite letras, números, espacios, puntos, comas y guiones.")
    private String color;

    @Size(max=80, message = "El nombre no puede tener más de 80 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,-]+$", message = "El campo solo permite letras, números, espacios, puntos, comas y guiones.")
    private String notes;
}
