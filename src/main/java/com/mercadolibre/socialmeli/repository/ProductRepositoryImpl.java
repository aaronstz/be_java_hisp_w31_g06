package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.Post;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements IProductRepository {
    private Set<Post> posts;

    public ProductRepositoryImpl() {
        // Inicializar lista de posts.
    }

    @Override
    public void savePost(Post post) { // TODO ❤️

    }

    @Override
    public Set<Post> findRecentPostsForUser(Integer userId) {
        List<Post> posts = this.posts.stream()
                .filter(p -> p.getUserId().equals(userId) && p.getDate().isAfter(LocalDate.now().minusDays(14)))
                .toList();
        return Set.copyOf(posts);
    }

    @Override
    public Post findPostById(Integer postId) { // TODO ❤️
        return null;
    }
}
