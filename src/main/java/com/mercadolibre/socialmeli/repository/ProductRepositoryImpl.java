package com.mercadolibre.socialmeli.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.util.List;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Repository
public class ProductRepositoryImpl implements IProductRepository {
    private List<Product> listOfProducts = new ArrayList<>();

    public ProductRepositoryImpl() throws IOException {
        loadDataBase();
    }

    @Override
    public List<Product> findAll() {
        return listOfProducts;
    }

    @Override
    public void savePost(Post post) { // TODO ❤️

    }

    @Override
    public Set<Post> findRecentPostsForUser(Integer userId) {
        return null;
    }

    @Override
    public Post findPostById(Integer postId) { // TODO ❤️
        return null;
    }

    private void loadDataBase() throws IOException {
        File file;
        ObjectMapper mapper = new ObjectMapper();
        List<Product> productList;

        file = ResourceUtils.getFile("classpath:products.json");
        productList = mapper.readValue(file, new TypeReference<List<Product>>() {
        });

        listOfProducts = productList;
    }
}
