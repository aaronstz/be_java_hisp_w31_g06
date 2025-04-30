package com.mercadolibre.socialmeli.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Integer userId;
    private String userName;
    private Integer followersCount;
    private Set<Follow> follower;
    private Set<Follow> following;
    private Set<Post> post;
}
