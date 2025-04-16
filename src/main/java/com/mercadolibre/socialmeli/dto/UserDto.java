package com.mercadolibre.socialmeli.dto;

import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Integer userId;
    private String userName;
    private Integer followers_count;
    private Set<User> follower;
    private Set<User> following;
    private Set<Post> post;
}
