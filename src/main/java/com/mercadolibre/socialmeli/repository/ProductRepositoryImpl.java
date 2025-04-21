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
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements IProductRepository {
    private List<Product> listOfProducts = new ArrayList<>();
    private List<Post> listOfPosts = new ArrayList<>();

    public ProductRepositoryImpl() throws IOException {
        loadDataBase();
    }

    @Override
    public List<Product> findAllProducts() {
        return listOfProducts;
    }

    @Override
    public List<Post> findAllPosts() {
        return listOfPosts;
    }

    @Override
    public Integer createNewId() {
        if (listOfPosts.isEmpty()) {
            return 1;
        }
        Integer maxId = listOfPosts.stream()
                .map(Post::getPostId)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(0);
        return maxId + 1;
    }

    @Override
    public void savePost(Post post) {
        listOfPosts.add(post);
    }

    @Override
    public Boolean saveProduct(Product product) {
        if (exists(product)) {
            return false;
        }
        listOfProducts.add(product);
        return true;
    }

    public Boolean exists(Product product) {
        Product found = listOfProducts.stream().filter(p -> p.getProductId()
                .equals(product.getProductId())).findFirst().orElse(null);
        return found != null;
    }

    @Override
    public Set<Post> findRecentPostsForUser(Integer userId) {
        return null;
    }

    @Override
    public Post findPostById(Integer postId) {
        return null;
    }

    @Override
    public List<Post> findPromosBySeller(Integer userId) {
        return listOfPosts.stream()
                .filter(p -> p.getUserId().equals(userId))
                .filter(p -> p.getHasPromo() && p.getDiscount() != null)
                .collect(Collectors.toList());
    }

    private void loadDataBase() throws IOException {
        File productsFile;
        File postsFile;
        ObjectMapper mapper = new ObjectMapper();
        List<Product> productList;
        List<Post> postLists;

        productsFile = ResourceUtils.getFile("classpath:products.json");
        productList = mapper.readValue(productsFile, new TypeReference<List<Product>>() {
        });
        postsFile = ResourceUtils.getFile("classpath:posts.json");
        postLists = mapper.readValue(postsFile, new TypeReference<List<Post>>() {
        });

        listOfProducts = productList;
        listOfPosts = postLists;
    }
}
