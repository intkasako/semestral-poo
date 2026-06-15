package entities;

import enums.Dificuldade;

public abstract class Questao {
    private int id;
    private String enunciado;
    private String assunto;
    private Dificuldade dificuldade;
    private int pontuacao;


    public Questao(String enunciado, String assunto, int id, Dificuldade dificuldade) {
        this.enunciado = enunciado;
        this.assunto = assunto;
        this.id = id;
        this.dificuldade = dificuldade;
        this.pontuacao = dificuldade.getValor();
    }

    public abstract boolean verificarResposta(String respostaDoUsuario);

    public int calcularPontuacao() {
        return pontuacao;
    }

    //Getters e Setters.

    public String getEnunciado() {
        return enunciado;
    }

    public String getAssunto() {
        return assunto;
    }

    public int getId(){
        return id;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }
}
