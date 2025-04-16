package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercadolibre.socialmeli.service.IUserService;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    public UserController(IUserService userServices) {
        this.userService = userServices;
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllPosts() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<?> follow(@PathVariable int userId, @PathVariable int userIdToFollow){
        return new ResponseEntity<>(userService.follow(userId, userIdToFollow), HttpStatus.OK);
    }
    @PutMapping("/users/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<?> unfollowUser(@PathVariable int userId,
                                          @PathVariable int userIdToUnfollow ){
        userService.unFollow(userId, userIdToUnfollow);
        return new ResponseEntity<>( "Fue eliminado exitosamente el usuario",HttpStatus.OK);


    }
}
