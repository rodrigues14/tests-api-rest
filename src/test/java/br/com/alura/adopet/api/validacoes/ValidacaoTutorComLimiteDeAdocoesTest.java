package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @InjectMocks
    private ValidacaoTutorComLimiteDeAdocoes validacao;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Spy
    private List<Adocao> adocoes = new ArrayList<>();

    @Mock
    private Adocao adocao;

    @Mock
    private Tutor tutor;

    @Mock
    private Pet pet;

    @Test
    void deveRetornarExceptionQuandoOTutorPassaDoLimiteDeAdocao() {
        // ARRANGE
        adocao = new Adocao(tutor, pet, "Motivo");
        adocao.marcarComoAprovada();
        for (int i = 0; i < 5; i++) {
            adocoes.add(adocao);
        }

        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocoes);
        BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);

        // ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void naoDeveRetornarExceptionQuandoOTutorNaoPassaDoLimiteDeAdocao() {
        // ARRANGE
        adocao = new Adocao(tutor, pet, "Motivo");
        adocao.marcarComoAprovada();
        for (int i = 0; i < 3; i++) {
            adocoes.add(adocao);
        }

        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocoes);
        BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);

        // ASSERT + ACT
        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

}