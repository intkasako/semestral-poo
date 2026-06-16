package entities;

import enums.Dificuldade;

public class QuestaoVerdadeiroFalso extends Questao{
    private char respostaCorreta;

    public QuestaoVerdadeiroFalso(String enunciado, String assunto, int id,
                                  char respostaCorreta, Dificuldade dificuldade) {
        super(enunciado, assunto, id, dificuldade);
        this.respostaCorreta = respostaCorreta;
    }

    @Override
    public boolean verificarResposta(String respostaDoUsuario) {
        String resposta = respostaDoUsuario.trim().toUpperCase();
        return !resposta.isEmpty() && resposta.charAt(0) == Character.toUpperCase(respostaCorreta);
    }


}
