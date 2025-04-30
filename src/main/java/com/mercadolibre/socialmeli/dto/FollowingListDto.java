package com.mercadolibre.socialmeli.dto;

import com.mercadolibre.socialmeli.entity.Follow;
import lombok.Data;

import java.util.Set;

@Data
public class FollowingListDto {
    private Integer userId;
    private String userName;
    private Set<Follow> followed;
}
