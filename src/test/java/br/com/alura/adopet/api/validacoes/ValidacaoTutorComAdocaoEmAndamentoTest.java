package br.com.alura.adopet.api.validacoes;


import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
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
class ValidacaoTutorComAdocaoEmAndamentoTest {

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @InjectMocks
    private ValidacaoTutorComAdocaoEmAndamento validacao;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Spy
    private List<Adocao> adocoes = new ArrayList<>();

    @Mock
    private Adocao adocao1;

    @Mock
    private Tutor tutor;

    @Test
    void deveRetornarUmaExceptionQuandoOTutorTemUmaAdocaoEmAndamento () {
        // ARRANGE
        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocoes);
        adocoes.add(adocao1);
        BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        BDDMockito.given(adocao1.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao1.getStatus()).willReturn(StatusAdocao.AGUARDANDO_AVALIACAO);

        // ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void naoDeveRetornarUmaExceptionQuandoOTutorNaoTemUmaAdocaoEmAndamento () {
        // ARRANGE
        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocoes);
        adocoes.add(adocao1);
        BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        BDDMockito.given(adocao1.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao1.getStatus()).willReturn(StatusAdocao.APROVADO);

        // ASSERT + ACT
        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

}