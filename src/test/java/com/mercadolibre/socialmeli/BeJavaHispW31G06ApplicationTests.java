package com.mercadolibre.socialmeli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.MensajeDto;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.Assertions;
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
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

	@Test
	void testFollow() throws Exception {
		int userId1 = 1;
		int userId2 = 2;

		String expected = "{\"message\": \"El usuario 1 siguió al usuario 2\"}";

		MensajeDto response = new ObjectMapper().readValue(expected, MensajeDto.class);

		this.mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}", userId1, userId2))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value(response.getMessage()))
				.andReturn();
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


	@DisplayName("getFollowersCount should return the number of followers of the user when the input data is valid")
	@Test
	public void getFollowersCount_shouldReturnCorrectFollowerCount_whenInputIsValid() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/users/1/followers/count"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.followersCount").value(2))
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		assertNotNull(responseBody);

		ObjectMapper objectMapper = new ObjectMapper();
		FollowerCountDto responseDto = objectMapper.readValue(responseBody, FollowerCountDto.class);

		assertTrue(responseDto.getFollowersCount() >= 0);
		Assertions.assertEquals(2, responseDto.getFollowersCount());
	}

	@DisplayName("getRecentSellerPostsForUser Should return the posts sorted in ascending order by date.")
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

		Assertions.assertEquals(fechasOrdenadas, fechas);
	}


	@DisplayName("getRecentSellerPostsForUser Should return the posts sorted in descending order by date.")
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

		Assertions.assertEquals(fechasOrdenadas, fechas);

	}


}
