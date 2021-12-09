package com.facuferro.meetup.meetup.integration;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.facuferro.meetup.domain.Location;
import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
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

@SpringBootTest
@AutoConfigureMockMvc
public class MeetupIntegrationTest extends MeetupAbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void returnAllMeetups() throws Exception {
        this.mockMvc.perform(get("/meetups/")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getTemperature() throws Exception {
        this.mockMvc.perform(get("/meetups/1/temperature")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getTemperatureNotFoundMeetup() throws Exception {
        this.mockMvc.perform(get("/meetups/100/temperature")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void createMeetupWithError() throws Exception {

        String token = getToken();
        Meetup meetup = getBasicMeetup(101l);
        Location location = new Location(999l,"",0d,0d);
        meetup.setLocation(location);
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/meetups")
                        .header("authorization",  token)
                        .content(asJsonString(meetup))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andDo(print());

    }

    @Test
    public void createMeetup() throws Exception {

        String token = getToken();
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/meetups")
                        .header("authorization",  token)
                        .content(asJsonString(getBasicMeetup(100l)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
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

    @Test
    public void calculateBeerForbiden() throws Exception {
        this.mockMvc.perform(get("/meetups/100/beers/calculate")).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    public void calculateBeer() throws Exception {
        String token = getToken();
        this.mockMvc.perform(get("/meetups/1/beers/calculate")
                .header("authorization",  token))
                .andDo(print()).andExpect(status().isOk());
    }

}
