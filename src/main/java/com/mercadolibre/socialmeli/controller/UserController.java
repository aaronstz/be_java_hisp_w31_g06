package com.mercadolibre.socialmeli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.socialmeli.service.IUserService;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @PutMapping("/users/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<?> unfollowUser(@PathVariable int userId,
                                          @PathVariable int userIdToUnfollow ){
        userService.unFollow(userId, userIdToUnfollow);
        return new ResponseEntity<>( "Fue eliminado exitosamente el usuario",HttpStatus.OK);


    }
}
