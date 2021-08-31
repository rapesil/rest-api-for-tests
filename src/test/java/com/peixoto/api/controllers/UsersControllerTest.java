package com.peixoto.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peixoto.api.domain.Users;
import com.peixoto.api.dto.UserDto;
import com.peixoto.api.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UsersController usersController;

    @Mock
    private UserService userService;

    private JacksonTester<Users> usersJacksonTester;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }

    @Test
    void listAllBooks_successfullyOneBook() throws Exception {
        Mockito.when(userService.listAllUsers()).thenReturn((List<UserDto>) List.of(new UserDto()));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
            .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        List<UserDto> users = mapper.readValue(response.getContentAsString(), new TypeReference<List<UserDto>>() {});

        Assertions.assertThat(response.getStatus()).isEqualTo(200);
        Assertions.assertThat(users.size()).isEqualTo(1);
    }
}
