package com.koiteampro.koipondcons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.models.request.LoginRequest;
import com.koiteampro.koipondcons.models.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class KoipondconsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testDemoEndpoint() throws Exception {
		mockMvc.perform(get("/api/demo"))
				.andExpect(status().isOk())
				.andExpect(content().string("Okeee"));
	}

	@Test
	void testLoginEndpoint() throws Exception {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("trung@gmail.com");
		loginRequest.setPassword("123456");

		mockMvc.perform(post("/api/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"name\":\"Nguyễn Thế Bình\"}"));
	}

	@Test
	void testRegisterEndpoint() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setName("John Doe 2");
		registerRequest.setEmail("john.doe2@example.com");
		registerRequest.setPhone("84912345676");
		registerRequest.setPassword("password123");
		registerRequest.setRole(Role.CUSTOMER);

		mockMvc.perform(post("/api/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerRequest)))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"name\":\"John Doe 2\"}"));
	}

	@Test
	void testGetAllCombos() throws Exception {
		mockMvc.perform(get("/api/combos"))
				.andExpect(status().isOk());
	}

	@Test
	void testPromotionById() throws Exception {
		mockMvc.perform(get("/api/promotions/7"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"id\":7}"));
	}


}
