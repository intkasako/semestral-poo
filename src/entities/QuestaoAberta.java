package entities;

import enums.Dificuldade;

public class QuestaoAberta extends Questao{
    private String gabarito;

    public QuestaoAberta(String enunciado, String assunto, int id, String gabarito, Dificuldade dificuldade) {
        super(enunciado, assunto, id,dificuldade);
        this.gabarito= gabarito;
    }

    @Override
    public boolean verificarResposta(String respostaDoUsuario) {
        return false;
    }
}
