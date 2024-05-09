package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @Mock
    private TutorRepository tutorRepository;

    @InjectMocks
    private TutorService tutorService;

    @Mock
    private CadastroTutorDto cadastroTutorDto;

    @Mock
    private AtualizacaoTutorDto atualizacaoTutorDto;

    @Mock
    private Tutor tutor;

    @Test
    void deveriaCadastrarUmTutor() {

        BDDMockito.given(tutorRepository
                .existsByTelefoneOrEmail(cadastroTutorDto.email(), cadastroTutorDto.telefone()))
                .willReturn(false);

        assertDoesNotThrow(() -> tutorService.cadastrar(cadastroTutorDto));
        BDDMockito.then(tutorRepository).should().save(new Tutor(cadastroTutorDto));
    }

    @Test
    void deveriaAtualizarOTutor() {
        BDDMockito.given(tutorRepository
                .getReferenceById(atualizacaoTutorDto.id()))
                .willReturn(tutor);

        tutorService.atualizar(atualizacaoTutorDto);

        BDDMockito.then(tutor).should().atualizarDados(atualizacaoTutorDto);
    }

}