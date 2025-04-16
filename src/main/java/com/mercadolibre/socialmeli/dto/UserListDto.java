package com.mercadolibre.socialmeli.dto;

import com.mercadolibre.socialmeli.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserListDto {

    private Integer userId;
    private String userName;
    private Set<User> follower;

}
