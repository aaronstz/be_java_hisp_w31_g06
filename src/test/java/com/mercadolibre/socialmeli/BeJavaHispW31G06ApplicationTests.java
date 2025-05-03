package com.mercadolibre.socialmeli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BeJavaHispW31G06ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepositoryImpl productRepository;

	@BeforeEach
	void setUp() {
		TestDataFactory.createSixPosts().forEach(productRepository::savePost);
	}
	@DisplayName("Should return only promotional posts when calling /products/promotions successfully")
	@Test
	void getAllPromotions_ShouldReturnOnlyPromoPosts_WhenCalledSuccessfully() throws Exception {
		String responseJson = mockMvc.perform(get("/products/promotions")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", not(empty())))
				.andExpect(jsonPath("$[*].hasPromo", everyItem(is(true))))
				.andExpect(jsonPath("$[*].discount", everyItem(greaterThan(0.0))))
				.andReturn()
				.getResponse()
				.getContentAsString();

		// Assert
		List<PostDto> responseList = TestDataFactory.parsePostDtoList(responseJson);
		assertFalse(responseList.isEmpty());
		assertTrue(responseList.stream().allMatch(p -> p.getHasPromo() && p.getDiscount() > 0));
	}


	@DisplayName("getRecentSellerPostsForUser debería devolver posts en orden ascendente cuando el parámetro 'order' es 'date_asc'")
	@Test
	void getRecentSellerPostsForUser_shouldReturnPostsInAscendingOrder_whenOrderIsAscending() throws Exception {
		Integer userId = 1;

		MvcResult result = mockMvc.perform(get("/products/followed/{userId}/list", userId)
						.param("order", "date_asc"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(json);
		JsonNode posts = root.get("postDto");

		List<LocalDate> fechas = new ArrayList<>();
		for (JsonNode post : posts) {
			fechas.add(LocalDate.parse(post.get("date").asText()));
		}

		List<LocalDate> fechasOrdenadas = new ArrayList<>(fechas);
		fechasOrdenadas.sort(Comparator.naturalOrder());

		assertEquals(fechasOrdenadas, fechas);
	}


	@DisplayName("getRecentSellerPostsForUser debería devolver posts en orden descendente cuando el parámetro 'order' es 'date_desc'")
	@Test
	void getRecentSellerPostsForUser_shouldReturnPostsInDescendingOrder_whenOrderIsDescending() throws Exception {
		Integer userId = 1;
		MvcResult result = mockMvc.perform(get("/products/followed/{userId}/list", userId)
						.param("order", "date_desc"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(json);
		JsonNode posts = root.get("postDto");

		List<LocalDate> fechas = new ArrayList<>();
		for (JsonNode post : posts) {
			fechas.add(LocalDate.parse(post.get("date").asText()));
		}

		List<LocalDate> fechasOrdenadas = new ArrayList<>(fechas);
		fechasOrdenadas.sort(Comparator.reverseOrder());

		assertEquals(fechasOrdenadas, fechas);
	}


}
