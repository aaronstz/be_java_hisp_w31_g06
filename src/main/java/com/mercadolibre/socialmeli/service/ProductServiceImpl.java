package com.mercadolibre.socialmeli.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mercadolibre.socialmeli.dto.*;
import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.exception.BadRequestException;
import com.mercadolibre.socialmeli.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.IProductRepository;
import com.mercadolibre.socialmeli.repository.IUserRepository;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IUserRepository userRepository;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());;


    @Override
    public List<ProductDto> getAll() {
        List<Product> allProducts = productRepository.findAllProducts();

        if (allProducts.isEmpty()) {
            throw new NotFoundException("No se encontraron productos.");
        }

        return allProducts.stream().map(p -> mapper.convertValue(p, ProductDto.class)).toList();
    }

    @Override
    public PostDto createPost(CreatePostDto postDto) {
        Post post = mapper.convertValue(postDto, Post.class);

        Boolean saved = productRepository.saveProduct(post.getProduct());
        if (!saved) {
            throw new ConflictException("Ya existe un producto con el id " + post.getProduct().getProductId());
        }

        if (!userRepository.addPostToUser(post))
            throw new NotFoundException("No se encontró al usuario");
        if (post.getHasPromo() == null)
            post.setHasPromo(false);
        if (post.getDiscount() == null)
            post.setDiscount(0D);

        productRepository.savePost(post);
        post.setPostId(productRepository.findAllPosts().size() + 1);

        PostDto createdPost = mapper.convertValue(post, PostDto.class);
        createdPost.setDate(LocalDate.now());
        return createdPost;
    }

    public List<PostDto> orderPosts(List<PostDto> posts, String order) {
        if (order.equalsIgnoreCase("date_asc")) {
            return posts.stream()
                    .sorted((p1, p2) -> p1.getDate().compareTo(p2.getDate()))
                    .collect(Collectors.toList());
        } else {
            return posts.stream()
                    .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public FollowingPostDto getRecentSellerPostsForUser(Integer userId, String order) {
        if (!order.equals("date_asc") && !order.equals("date_desc")) {
            throw new BadRequestException("El orden solo puede ser 'date_asc' o 'date_desc'");
        }

        User user = getUserById(userId);

        if (user == null) {
            throw new NotFoundException("No se encontró un usuario con ID: " + userId);
        }

        Set<Follow> followingList = user.getFollowing();
        if (followingList == null || followingList.isEmpty()) {
            throw new NotFoundException("No se encontraron seguidos para el usuario con ID: " + userId);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<PostDto> allRecentPosts = followingList.stream()
                .map(s -> userRepository.findRecentPostsForUser(s.getUserId()))
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .map(post -> {
                    LocalDate postDate = LocalDate.parse(post.getDate(), formatter);
                    PostDto postDto = mapper.convertValue(post, PostDto.class);
                    postDto.setDate(postDate);
                    return postDto;
                })
                .toList();


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
                .filter(Post::getHasPromo)
                .collect(Collectors.toSet()).size();
        return new PromoPostCountDto(user_id, user.getUserName(), count);
    }

    @Override
    public PromoPostDto getPromosBySeller(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("No se encontró el usuario con id " + userId);
        }
        List<Post> promoPosts = productRepository.findPromosBySeller(userId);
        if (promoPosts.isEmpty()) {
            throw new NotFoundException("No se encontraron promociones del vendedor con id " + userId);
        }
        PromoPostDto foundPromo = new PromoPostDto();
        foundPromo.setUserId(userId);
        foundPromo.setUserName(userRepository.findUserById(userId).getUserName());

        List<PostDto> mappedPosts = promoPosts.stream()
                .map(p -> mapper.convertValue(p, PostDto.class))
                .collect(Collectors.toList());

        foundPromo.setPosts(mappedPosts);
        return foundPromo;
    }

    @Override
    public List<PostDto> getAllPromos() {
        List<Post> promoPosts = productRepository.getAllPromos();
        if (promoPosts.isEmpty()) {
            throw new NotFoundException("No se encontraron promociones");
        }
        return promoPosts.stream().map(p -> mapper.convertValue(p, PostDto.class)).toList();
    }

    @Override
    public FollowingPostDto getSellerPostsForUserByKeyword(Integer userId, String keyword) {
        User user = getUserById(userId);

        Set<Follow> followingList = user.getFollowing();
        if (followingList == null || followingList.isEmpty()) {
            throw new NotFoundException("No se encontraron seguidos para el usuario con ID: " + userId);
        }

        List<PostDto> filteredPosts = followingList.stream()
                .map(seller -> userRepository.findPostsByKeyword(seller.getUserId(), keyword))
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .map(post -> mapper.convertValue(post, PostDto.class))
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new NotFoundException(
                    "No se encontraron publicaciones que contengan la palabra clave '" + keyword
                            + "' para los seguidos del usuario con ID: " + userId);
        }

        FollowingPostDto result = new FollowingPostDto();
        result.setUserId(userId);
        result.setPostDto(filteredPosts);
        return result;
    }

    private User getUserById(Integer userId) {
        return Optional.ofNullable(userRepository.findUserById(userId))
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));
    }

    @Override
    public FollowingPostDto getSellerPostsForUserByCategory(Integer userId, Integer categoryId) {
        if (categoryId == null || categoryId <= 0) {
            throw new BadRequestException("Debe proporcionar una categoría válida.");
        }

        Set<Post> posts = userRepository.findPostsByFollowedUsersAndCategory(userId, categoryId);

        if (posts == null || posts.isEmpty()) {
            throw new NotFoundException("No se encontraron publicaciones para la categoría proporcionada.");
        }

        List<PostDto> postDtos = posts.stream()
                .map(post -> mapper.convertValue(post, PostDto.class))
                .collect(Collectors.toList());

        FollowingPostDto result = new FollowingPostDto();
        result.setUserId(userId);
        result.setPostDto(postDtos);
        return result;
    }
}