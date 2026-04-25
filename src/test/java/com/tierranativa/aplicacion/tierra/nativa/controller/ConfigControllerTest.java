package com.tierranativa.aplicacion.tierra.nativa.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getContactInfo_ReturnsOk() throws Exception {
        mockMvc.perform(get("/configs/contact")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getContactInfo_ReturnsWhatsappField() throws Exception {
        mockMvc.perform(get("/configs/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.whatsapp").exists())
                .andExpect(jsonPath("$.whatsapp").value(not(emptyString())));
    }

    @Test
    void getContactInfo_WhatsappContainsOnlyDigits() throws Exception {
        mockMvc.perform(get("/configs/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.whatsapp").value(matchesPattern("[0-9]+")));
    }

    @Test
    void getContactInfo_ReturnsMessageField() throws Exception {
        mockMvc.perform(get("/configs/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(not(emptyString())));
    }

    @Test
    void getContactInfo_ContentTypeIsJson() throws Exception {
        mockMvc.perform(get("/configs/contact"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getContactInfo_IsPublic_NoAuthRequired() throws Exception {
        mockMvc.perform(get("/configs/contact"))
                .andExpect(status().isOk());
    }

    @Test
    void getContactInfo_ReturnsExactWhatsappNumber() throws Exception {
        mockMvc.perform(get("/configs/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.whatsapp").value("5491171256075"));
    }
}