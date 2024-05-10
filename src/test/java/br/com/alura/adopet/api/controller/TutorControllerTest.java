package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.service.TutorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
class TutorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TutorService tutorService;

    @Test
    void deveriaCadastrarUmTutor() throws Exception {
        // ARRANGE
        String json = """
                {
                    "nome": "Teste",
                    "telefone": "(11)98888-8888",
                    "email": "email@email.com"
                }
                """;

        // ACT
        var response = mvc.perform(
                post("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void naoDeveriaCadastrarTutorComInformacoesInvalidas() throws  Exception {
        // ARRANGE
        String json = "{}";

        // ACT
        var response = mvc.perform(
                post("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaAtualizarOTutor() throws Exception {
        // ARRANGE
        String json = """
                {
                    "id": 1,
                    "nome": "Teste atualizacao",
                    "telefone": "(11)98888-8888",
                    "email": "email@email.com"
                }
                """;
        // ACT
        var response = mvc.perform(
                put("/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

}