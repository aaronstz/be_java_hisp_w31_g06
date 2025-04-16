package com.mercadolibre.socialmeli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.socialmeli.service.IUserService;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;

}
