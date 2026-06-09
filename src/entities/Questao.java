package entities;

import enums.Dificuldade;

public abstract class Questao {
    private int id;
    private String enunciado;
    private String assunto;
    private Dificuldade dificuldade;


    public Questao(String enunciado, String assunto, int id, Dificuldade dificuldade) {
        this.enunciado = enunciado;
        this.assunto = assunto;
        this.id = id;
        this.dificuldade = dificuldade;
    }

    public abstract boolean verificarResposta(String respostaDoUsuario);

    public String getEnunciado() {
        return enunciado;
    }

    public String getAssunto() {
        return assunto;
    }

    public int getId(){
        return id;
    }
}
