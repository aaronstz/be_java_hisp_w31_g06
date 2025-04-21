package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;

import java.util.List;
import java.util.Set;

public interface IProductRepository {
    List<Product> findAllProducts();

    List<Post> findAllPosts();

    Integer createNewId();

    void savePost(Post post);

    Boolean saveProduct(Product product);

    Set<Post> findRecentPostsForUser(Integer userId);

    Post findPostById(Integer postId);

    List<Post> findPromosBySeller(Integer userId);
}
