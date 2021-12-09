package com.facuferro.meetup.meetup.integration;


import com.facuferro.meetup.domain.Role;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserState;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import com.facuferro.meetup.security.PasswordUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest extends MeetupAbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createUser() throws Exception {

        User user =  User.builder()
                .name("Facundo")
                .surname("Ferro")
                .email("facundoferro1@gmail.com")
                .pwd(PasswordUtils.encrypt("password"))
                .role(Role.ROLE_ADMIN)
                .status(UserState.ACTIVE)
                .token("token")
                .build();

        String token = getToken();
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/users")
                        .header("authorization",  token)
                        .content(asJsonString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void confirmUserError() throws Exception {

        mockMvc.perform( MockMvcRequestBuilders
                        .get("/users/1/confirm/anyToken")
                        .content(asJsonString(getBasicUser()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    protected String getToken() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("user", "facundoferro@gmail.com")
                        .param("password", "facu"))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        User user = new ObjectMapper().readValue(json, User.class);
        return user.getToken();
    }


}
