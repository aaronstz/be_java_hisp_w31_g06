package com.mercadolibre.socialmeli.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.dto.ProductDto;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.IProductRepository;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService{

    IProductRepository productRepository;

    public ProductServiceImpl(ProductRepositoryImpl productRepository) {
        this.productRepository = productRepository;
    };

    @Override
    public List<ProductDto> getAll() {
        List<Product> allProducts = productRepository.findAll();

        if(allProducts.isEmpty()) {
            throw new NotFoundException("No se encontraron productos.");
        }

        ObjectMapper mapper = new ObjectMapper();
        return allProducts.stream().map(p -> mapper.convertValue(p, ProductDto.class)).toList();
    }

    @Override
    public String createPost(Post post) { //TODO ❤️
        return "";
    }

    @Override
    public FollowingPostDto getPost(Integer userId) {//TODO ❤️
        return null;
    }
}
