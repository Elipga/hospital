package com.example.hospital.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DoctorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllDoctors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}