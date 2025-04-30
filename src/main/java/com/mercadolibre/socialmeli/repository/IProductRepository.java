package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;

import java.util.List;

public interface IProductRepository {
    List<Product> findAllProducts();
    List<Post> findAllPosts();
    void savePost(Post post);
    Boolean saveProduct(Product product);
    List<Post> getAllPromos();
    List<Post> findPromosBySeller(Integer userId);
    void clearRepository();
}
