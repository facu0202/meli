package com.facuferro.meetup.meetup.integration;


import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest extends MeetupAbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void returnAllMeetups() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("user", "facundoferro@gmail.com")
                        .param("password", "facu123"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void invalidToken() throws Exception {
        String token = "aaaa";
        this.mockMvc.perform(get("/meetups/1/beers/calculate")
                        .header("authorization",  token))
                .andDo(print()).andExpect(status().isForbidden());
    }


}
