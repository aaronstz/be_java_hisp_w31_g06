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
    private Set<User> follower; //no se si esto es una lista de usuarios o de userID
    private Set<User> following; //me corrigieron de que es following y no followed
    private Set<Post> post;
}
