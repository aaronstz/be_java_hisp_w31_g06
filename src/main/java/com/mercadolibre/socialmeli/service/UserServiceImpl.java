package com.mercadolibre.socialmeli.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.UserDto;
import com.mercadolibre.socialmeli.dto.UserListDto;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.exception.BadRequestException;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.IProductRepository;
import com.mercadolibre.socialmeli.repository.IUserRepository;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
@Service
public class UserServiceImpl implements IUserService {

    IUserRepository userRepository;

    @Override
    public void follow(Integer userId, Integer userIdToFollow) {//TODO ❤️

    }

    @Override
    public FollowerCountDto getFollowersCount(Integer userId) {//TODO ❤️
        return null;
    }

    @Override
    public UserListDto getFollowersList(Integer userId) {//TODO ❤️
        return null;
    }

    @Override
    public UserListDto getFollowedList(Integer userId) {//TODO ❤️
        return null;
    }
private UserDto mapToDto(User user){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(user, UserDto.class);
}
    @Override
    public String unFollow(Integer userId, Integer userIdToUnfollow) {

        User user = userRepository.findUserById(userIdToUnfollow)
                .orElseThrow(() -> new NotFoundException("Usuario con ID " + userIdToUnfollow + " no encontrado"));

        if (!user.getFollowing().contains(user)) {
            throw new BadRequestException("El usuario " + userId + " no sigue al usuario " + userIdToUnfollow);
        }

        userRepository.removeFollow(user, user);

        return "El usuario " + userId + " dejó de seguir al usuario " + userIdToUnfollow + " correctamente.";
    }
}

