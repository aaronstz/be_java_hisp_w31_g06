package com.mercadolibre.socialmeli.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.exception.ConflictException;
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
import com.mercadolibre.socialmeli.dto.PromoPostCountDto;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;

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
    public PostDto createPost(PostDto postDto) {
        ObjectMapper mapper = new ObjectMapper();

        Post post = mapper.convertValue(postDto, Post.class);

        Boolean saved = productRepository.saveProduct(post.getProduct());
        if (!saved) {
            throw new ConflictException("Ya existe un producto con el id " + post.getProduct().getProductId());
        }

        if (!userRepository.addPostToUser(post))
            throw new NotFoundException("No se encontró al usuario");
        if(post.getHasPromo() == null) post.setHasPromo(false);
        if(post.getDiscount() == null) post.setDiscount(0D);

        if(!userRepository.addPostToUser(post)) throw new NotFoundException("No se encontró al usuario");

        productRepository.savePost(post);
        post.setPostId(productRepository.findAllPosts().size() + 1);

        return mapper.convertValue(post, PostDto.class);
    }

    private List<PostDto> orderPosts(List<PostDto> posts, String order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (order.equalsIgnoreCase("date_asc")) {
            return posts.stream()
                    .sorted((p1, p2) -> LocalDate.parse(p1.getDate(), formatter).compareTo(LocalDate.parse(p2.getDate(),
                            formatter)))
                    .collect(Collectors.toList());
        }
        return posts.stream()
                .sorted((p1, p2) -> LocalDate.parse(p2.getDate(), formatter).compareTo(LocalDate.parse(p1.getDate(),
                        formatter)))
                .collect(Collectors.toList());
    }

    @Override
    public FollowingPostDto getRecentSellerPostsForUser(Integer userId, String order) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("No se encontró un usuario con ID: " + userId);
        }

        Set<Follow> followingList = user.getFollowing();
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
        result.setPostDto(orderPosts(allRecentPosts, order));
        return result;
    }

    @Override
    public PromoPostCountDto getPromoPostCount(Integer user_id) {
        User user = userRepository.findUserById(user_id);
        if (user == null) {
            throw new NotFoundException("No se encontró un usuario con ID: " + user_id);
        }

        Set<Post> posts = userRepository.findRecentPostsForUser(user_id);
        if (posts == null || posts.isEmpty()) {
            throw new NotFoundException("No se encontraron publicaciones para el usuario con ID " + user_id);
        }

        Integer count = posts.stream()
                .filter(post -> post.getHasPromo())
                .collect(Collectors.toSet()).size();
        return new PromoPostCountDto(user_id, user.getUserName(), count);
    }
}
