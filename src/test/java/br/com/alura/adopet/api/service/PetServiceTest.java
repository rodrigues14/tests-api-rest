package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @Mock
    private Abrigo abrigo;

    @Mock
    private CadastroPetDto cadastroPetDto;

    @Test
    void deveriaListarOsPetsDisponiveis() {
        // ACT
        petService.buscarPetsDisponiveis();

        // ASSERT
        BDDMockito.then(petRepository).should().findAllByAdotadoFalse();
    }

    @Test
    void deveriaCadastrarUmPet() {
        petService.cadastrarPet(abrigo, cadastroPetDto);

        BDDMockito.then(petRepository).should().save(new Pet(cadastroPetDto, abrigo));
    }

}