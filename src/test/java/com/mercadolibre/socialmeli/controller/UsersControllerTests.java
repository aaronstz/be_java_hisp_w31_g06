package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.service.UserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UsersControllerTests {

    @Mock
    private UserServiceImpl service;

    @InjectMocks
    private UserController controller;
}
