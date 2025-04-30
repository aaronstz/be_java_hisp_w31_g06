package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepositoryImpl repository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void orderPosts_dateAsc_shouldSortCorrectly() {
        LocalDate date1 = LocalDate.of(2023, 10, 25);
        LocalDate date2 = LocalDate.of(2023, 10, 26);
        LocalDate date3 = LocalDate.of(2023, 10, 24);

        PostDto post1 = new PostDto();
        post1.setDate(date1);
        PostDto post2 = new PostDto();
        post2.setDate(date2);
        PostDto post3 = new PostDto();
        post3.setDate(date3);

        List<PostDto> sortedPosts = service.orderPosts(List.of(post1, post2, post3), "date_asc");

        assertEquals(date3, sortedPosts.get(0).getDate());
        assertEquals(date1, sortedPosts.get(1).getDate());
        assertEquals(date2, sortedPosts.get(2).getDate());
    }

    @Test
    void orderPosts_dateDesc_shouldSortCorrectly() {
        LocalDate date1 = LocalDate.of(2023, 10, 25);
        LocalDate date2 = LocalDate.of(2023, 10, 26);
        LocalDate date3 = LocalDate.of(2023, 10, 24);

        PostDto post1 = new PostDto();
        post1.setDate(date1);
        PostDto post2 = new PostDto();
        post2.setDate(date2);
        PostDto post3 = new PostDto();
        post3.setDate(date3);

        List<PostDto> sortedPosts = service.orderPosts(List.of(post1, post2, post3), "date_desc");

        assertEquals(date2, sortedPosts.get(0).getDate());
        assertEquals(date1, sortedPosts.get(1).getDate());
        assertEquals(date3, sortedPosts.get(2).getDate());
    }

    @Test
    void orderPosts_emptyList_shouldReturnEmptyList() {
        List<PostDto> posts = List.of();
        List<PostDto> sortedPosts = service.orderPosts(posts, "date_asc");
        assertEquals(0, sortedPosts.size());
    }

    @Test
    void orderPosts_sameDates_shouldMaintainOrder() {
        LocalDate date1 = LocalDate.of(2023, 10, 25);
        LocalDate date2 = LocalDate.of(2023, 10, 25);
        LocalDate date3 = LocalDate.of(2023, 10, 25);

        PostDto post1 = new PostDto();
        post1.setDate(date1);
        PostDto post2 = new PostDto();
        post2.setDate(date2);
        PostDto post3 = new PostDto();
        post3.setDate(date3);

        List<PostDto> posts = Arrays.asList(post1, post2, post3);
        List<PostDto> sortedPosts = service.orderPosts(posts, "date_asc");

        assertEquals(date1, sortedPosts.get(0).getDate());
        assertEquals(date2, sortedPosts.get(1).getDate());
        assertEquals(date3, sortedPosts.get(2).getDate());
    }
}
