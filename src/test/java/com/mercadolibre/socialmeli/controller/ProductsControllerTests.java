package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.service.ProductServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductsControllerTests {

    @Mock
    private ProductServiceImpl service;

    @InjectMocks
    private ProductsController controller;
}
