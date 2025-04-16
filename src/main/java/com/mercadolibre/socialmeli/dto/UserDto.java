package com.mercadolibre.socialmeli.dto;

import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto implements Serializable {
    private Integer userId;
    private String userName;
    private Integer followersCount;
    private Set<FollowDto> follower;
    private Set<FollowDto> following;
    private Set<Post> post;
}
