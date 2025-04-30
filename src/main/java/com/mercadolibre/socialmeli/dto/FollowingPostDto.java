package com.mercadolibre.socialmeli.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowingPostDto implements Serializable {

        @NotNull(message = "El ID no puede estar vacío.")
        @Positive(message = "El ID debe ser mayor a 0.")
        private Integer userId;

        private List<PostDto> postDto;
}
