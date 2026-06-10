package entities;

import enums.Dificuldade;
import enums.Letra;

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
        for(Alternativa alternativa : alternativas) {
        	if(alternativa.isVeracidade() == true) {
        		return true;
        	}else {
                continue;
            }
        }
        return false;
    }

    public void addAlternativa(String texto, Letra letra, boolean veracidade) {
        alternativas.add(new Alternativa(texto, letra, veracidade));
    }
}

