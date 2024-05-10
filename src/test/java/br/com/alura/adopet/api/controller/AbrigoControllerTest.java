package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AbrigoControllerTest {

    @MockBean
    private AbrigoService abrigoService;

    @Autowired
    private MockMvc mvc;

    @Spy
    List<AbrigoDto> abrigos = new ArrayList<>();

    @Autowired
    private JacksonTester<List<AbrigoDto>> abrigosDtoJson;

    @Spy
    List<PetDto> petsDoAbrigo = new ArrayList<>();

    @Autowired
    private JacksonTester<List<PetDto>> petsDtoJson;

    @Autowired
    private JacksonTester<CadastroPetDto> cadastroPetDtoJson;

    @MockBean
    private PetService petService;


    @Test
    void deveriaListarOsAbrigos() throws Exception {
        // ARRANGE
        AbrigoDto abrigo1 = new AbrigoDto(1l, "abrigo1");
        AbrigoDto abrigo2 = new AbrigoDto(2l, "abrigo2");
        abrigos.add(abrigo1);
        abrigos.add(abrigo2);
        BDDMockito.given(abrigoService.listar()).willReturn(abrigos);

        var lista = abrigosDtoJson.write(abrigos).getJson();

        // ACT
        var response = mvc.perform(
                get("/abrigos")
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(lista, response.getContentAsString());
    }


    @Test
    void naoDeveriaCadastrarAbrigoComInformacoesInvalidas() throws Exception {
        // ARRANGE
        String json = "{}";

        // ACT
        var response = mvc.perform(
                post("/abrigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaCadastrarAbrigoComInformacoesValidas() throws Exception {
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
                post("/abrigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaListarOsPetsDeUmAbrigo() throws Exception {
        // ARRANGE
        String idOuNome = "petfeliz";
        PetDto pet = new PetDto(1l, TipoPet.GATO, "gato", "raca", 6);
        petsDoAbrigo.add(pet);
        BDDMockito.given(abrigoService.listarPetsDoAbrigo(idOuNome)).willReturn(petsDoAbrigo);

        var jsonEsperado = petsDtoJson.write(petsDoAbrigo).getJson();

        // ACT
        var response = mvc.perform(
                get("/abrigos/{idOuNome}/pets", idOuNome)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(jsonEsperado, response.getContentAsString());
    }

    @Test
    void deveriaCadastarUmPetEmUmAbrigo() throws Exception {
        // ARRANGE
        String idOuNome = "petfeliz";
        Abrigo abrigo = new Abrigo(10l, "petfeliz", "(11)98877-7788", "teste@teste.com");
        CadastroPetDto cadastroPetDto = new CadastroPetDto(TipoPet.GATO, "gato", "raca", 5, "branco", 10f);
        BDDMockito.given(abrigoService.carregarAbrigo(idOuNome)).willReturn(abrigo);

        // ACT
        petService.cadastrarPet(abrigo, cadastroPetDto);

        var response = mvc.perform(
                post("/abrigos/{idOuNome}/pets", idOuNome)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cadastroPetDtoJson.write(cadastroPetDto).getJson())
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

}