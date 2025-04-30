package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepositoryImpl repository;

    @InjectMocks
    private ProductServiceImpl service;
}
