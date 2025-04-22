package com.mercadolibre.socialmeli.dto;

import com.mercadolibre.socialmeli.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserListDto implements Serializable {

    private Integer userId;
    private String userName;
    private Set<Follow> follower;

}
