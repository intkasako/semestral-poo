package entities;

import enums.Dificuldade;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private List<Integer> quizzesConcluidosID = new ArrayList<>();
    private int pontuacao;

    public Usuario(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.pontuacao = 0;
    }

    public void addQuizzesConcluidosID(int id){
        this.quizzesConcluidosID.add(id);
    }

    public void mostrarQuizzesConcluidosID(){
        for (int a : quizzesConcluidosID){
            System.out.print(a);
        }
    }
    
    public void responderQuestao(Questao questao, String respostaDoUsuario, Dificuldade dificuldade) {
    	if(questao.verificarResposta(respostaDoUsuario) == true){
            adicionarPontuacao(dificuldade);
        }
    }

    public void adicionarPontuacao(Dificuldade dificuldade){
        this.pontuacao = pontuacao + dificuldade.getValor();
    }

    public void adicionarPontuacao(int pontos) {
        this.pontuacao += pontos;
    }

    public void adicionarPontuacaoPorQuestao(Questao questao) {
        this.pontuacao += questao.calcularPontuacao();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
    
    public int getPontuacao() {
        return pontuacao;
    }

  
}
