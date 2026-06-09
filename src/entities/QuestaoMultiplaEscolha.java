package entities;

import enums.Dificuldade;

import java.util.ArrayList;
import java.util.List;

public class QuestaoMultiplaEscolha extends Questao {
    private List<Alternativa> alternativas;

    public QuestaoMultiplaEscolha(String enunciado, String assunto, int id, Dificuldade dificuldade) {
        super(enunciado, assunto, id, dificuldade);
        this.alternativas = new ArrayList<>();
    }

    @Override
    public boolean verificarResposta(String respostaDoUsuario) {
        return false;
    }

    public void addAlternativa(String texto, boolean veracidade) {
        alternativas.add(new Alternativa(texto, veracidade));
    }
}

