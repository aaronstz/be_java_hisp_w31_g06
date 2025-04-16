package com.mercadolibre.socialmeli.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.IProductRepository;
import com.mercadolibre.socialmeli.repository.IUserRepository;
import com.mercadolibre.socialmeli.dto.ProductDto;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;
import com.mercadolibre.socialmeli.exception.AlreadyExistsException;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<ProductDto> getAll() {
        List<Product> allProducts = productRepository.findAllProducts();

        if (allProducts.isEmpty()) {
            throw new NotFoundException("No se encontraron productos.");
        }

        ObjectMapper mapper = new ObjectMapper();
        return allProducts.stream().map(p -> mapper.convertValue(p, ProductDto.class)).toList();
    }

    @Override
    public PostDto createPost(Post post) {
        Boolean saved = productRepository.saveProduct(post.getProduct());
        if (!saved) {
            throw new AlreadyExistsException("Ya existe un producto con el id " + post.getProduct().getProductId());
        }
        ObjectMapper mapper = new ObjectMapper();
        post.setPostId(productRepository.createNewId());
        productRepository.savePost(post);
        return mapper.convertValue(post, PostDto.class);
    }

    @Override
    public FollowingPostDto getRecentSellerPostsForUser(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("No se encontró un usuario con ID: " + userId);
        }

        Set<User> followingList = user.getFollowing();
        if (followingList == null || followingList.isEmpty()) {
            throw new NotFoundException("No se encontraron seguidos para el usuario con ID: " + userId);
        }

        ObjectMapper mapper = new ObjectMapper();
        List<PostDto> allRecentPosts = followingList.stream()
                .map(seller -> userRepository.findRecentPostsForUser(seller.getUserId()))
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .map(post -> mapper.convertValue(post, PostDto.class))
                .collect(Collectors.toList());

        if (allRecentPosts.isEmpty()) {
            throw new NotFoundException(
                    "No se encontraron publicaciones de las últimas 2 semanas para los seguidos del usuario con ID: "
                            + userId);
        }

        FollowingPostDto result = new FollowingPostDto();
        result.setUserId(userId);
        result.setPostDto(allRecentPosts);
        return result;
    }

}
