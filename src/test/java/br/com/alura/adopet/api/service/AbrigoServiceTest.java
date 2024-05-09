package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @Mock
    private AbrigoRepository abrigoRepository;

    @InjectMocks
    private AbrigoService abrigoService;

    @Captor
    private ArgumentCaptor<Abrigo> abrigoCaptor;

    @Mock
    private PetRepository petRepository;

    @Mock
    private Abrigo abrigo;

    @Test
    void deveriaChamarListaDeTodosOsAbrigos() {
        // ACT
        abrigoService.listar();

        // ASSERT
        BDDMockito.then(abrigoRepository).should().findAll();
    }

    @Test
    void deveriaCadastrarAbrigo() {
        // ARRANGE
        CadastroAbrigoDto dto = new CadastroAbrigoDto("Pet Feliz", "11988774455", "email@teste.com");

        BDDMockito.given(abrigoRepository
                .existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email()))
                .willReturn(false);

        // ACT
        abrigoService.cadatrar(dto);

        // ASSERT
        BDDMockito.then(abrigoRepository).should().save(abrigoCaptor.capture());
        Abrigo abrigoCadastrado = abrigoCaptor.getValue();
        Assertions.assertEquals(dto.nome(), abrigoCadastrado.getNome());
        Assertions.assertEquals(dto.telefone(), abrigoCadastrado.getTelefone());
        Assertions.assertEquals(dto.email(), abrigoCadastrado.getEmail());
    }

    @Test
    void naoDeveriaCadastrarAbrigoComDadosJaUsado() {
        // ARRANGE
        CadastroAbrigoDto dto = new CadastroAbrigoDto("Pet Feliz", "11988774455", "email@teste.com");

        BDDMockito.given(abrigoRepository
                        .existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email()))
                .willReturn(true);

        // ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> abrigoService.cadatrar(dto));
    }

    @Test
    void deveriaListarPetsDoAbrigo() {
        // ARRANGE
        Long id = 1l;
        BDDMockito.given(abrigoRepository.findById(id)).willReturn(Optional.of(abrigo));

        // ACT
        abrigoService.listarPetsDoAbrigo(String.valueOf(id));

        // ASSERT
        BDDMockito.then(petRepository).should().findByAbrigo(abrigo);
    }

}