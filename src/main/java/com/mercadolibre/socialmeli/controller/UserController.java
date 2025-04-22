package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.FollowingListDto;
import com.mercadolibre.socialmeli.dto.MensajeDto;
import com.mercadolibre.socialmeli.dto.UserDto;
import com.mercadolibre.socialmeli.dto.UserListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercadolibre.socialmeli.service.IUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllPosts() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<MensajeDto> follow(@PathVariable int userId, @PathVariable int userIdToFollow) {
        return new ResponseEntity<>(userService.follow(userId, userIdToFollow), HttpStatus.OK);
    }
  
    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<FollowingListDto> getFollowedList(@PathVariable Integer userId,
                                                             @RequestParam(defaultValue = "name_asc")String order) {
        return new ResponseEntity<>(userService.getFollowedList(userId, order), HttpStatus.OK);
    }

    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<UserListDto> getFollowersList(@PathVariable Integer userId,
                                                        @RequestParam(defaultValue = "name_asc") String order ) {
        return new ResponseEntity<>(userService.getFollowersList(userId, order), HttpStatus.OK);
    }
  
    @PutMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<?> unfollowUser(@PathVariable int userId,
                                          @PathVariable int userIdToUnfollow ){
        userService.unFollow(userId, userIdToUnfollow);
        return new ResponseEntity<>( "Fue eliminado exitosamente el usuario",HttpStatus.OK);
    }
  
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<?> getFollowersCount(@PathVariable int userId) {
        return new ResponseEntity<>(userService.getFollowersCount(userId), HttpStatus.OK);
    }
}
