package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.UserDto;
import com.mercadolibre.socialmeli.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {
 IUserService userService;

        @PutMapping("/users/{userId}/unfollow/{userIdToUnfollow}")
        public ResponseEntity<String> unfollowUser(@PathVariable int userId,
                                                 @PathVariable int userIdToUnfollow ){
            userService.unFollow(userId, userIdToUnfollow);
            return new ResponseEntity<>(HttpStatus.OK);


    }
}
