package services;

import entities.Questao;
import entities.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GerenciadorQuiz {
    private List<Questao> questoes;
    private int indiceAtual; //Controla qual questão da lista está sendo lida.
    private Usuario usuarioAtual;
    private Questao questaoAtual;
    private Boolean jogoFinalizado;
    private Integer acertos;
    private Integer pontuacaoPartida;

    public GerenciadorQuiz(List<Questao> questoes, Usuario usuarioAtual) {

        //Cria uma cópia da lista e embaralha todas as questões de forma aleatória.
        this.questoes = new ArrayList<>(questoes);

        //Embaralha a lista de questões.
        Collections.shuffle(this.questoes);
        
        this.indiceAtual = 0;
        this.usuarioAtual = usuarioAtual;
        this.jogoFinalizado = false;
        this.acertos = 0;
        this.pontuacaoPartida = 0;

        //Inicia o sorteio de questões.
        sortearProximaQuestao();
    }

    //      Getters e Setters

    public Questao getQuestaoAtual() { 
        return questaoAtual; 
    }
    public void setQuestaoAtual(Questao questaoAtual) { 
        this.questaoAtual = questaoAtual; 
    }

    public Boolean isJogoFinalizado() { 
        return jogoFinalizado; 
    }
    public void setJogoFinalizado(Boolean jogoFinalizado) { 
        this.jogoFinalizado = jogoFinalizado; 
    }


    public Integer getPontuacaoAtual() { 
        return pontuacaoPartida; 
    }
    public void setPontuacaoAtual(Integer pontuacaoAtual) { 
        this.pontuacaoPartida = pontuacaoAtual; 
    }

    public Integer getAcertos() { 
        return acertos; 
    }
    public void setAcertos(Integer acertos) { 
        this.acertos = acertos; 
    }

    public Usuario getUsuarioAtual() { 
        return usuarioAtual; 
    }
    public void setUsuarioAtual(Usuario usuarioAtual) { 
        this.usuarioAtual = usuarioAtual; 
    }


    //         Funções

    private void sortearProximaQuestao() {

        //Se a lista de questões estiver vazia, termina o jogo.
        if (questoes == null || questoes.isEmpty()) {
            questaoAtual = null;
            jogoFinalizado = true;
            somarPontuacaoPartidaAoUsuario();
            return;
        }

        //Ao chegar no final da lista de questões, reembaralha a lista inteira e volta para o início (índice 0).
        if (indiceAtual >= questoes.size()) {
            Collections.shuffle(this.questoes);
            indiceAtual = 0;                    
        }

        //Pega a questão na posição do índice atual e a define como a questão atual do jogo.
        questaoAtual = questoes.get(indiceAtual);
        
        //Aumenta o índice para a próxima questão.
        indiceAtual++; 
    }

    public boolean responder(String respostaDoUsuario) {
        if (questaoAtual == null || jogoFinalizado) {
            return false;
        }


        //Verifica resposta.
        boolean acertou = questaoAtual.verificarResposta(respostaDoUsuario);

        //Se acertou, soma à pontuação da partida + Número de acertos.
        if (acertou) {
            pontuacaoPartida += questaoAtual.calcularPontuacao();
            acertos++;
            sortearProximaQuestao(); //Traz a próxima pergunta
        } else {
            jogoFinalizado = true; //Errou, fim de jogo.
            somarPontuacaoPartidaAoUsuario(); //Soma ao usuário a pontuação da partida.
        }

        return acertou;
    }

    private void somarPontuacaoPartidaAoUsuario() {
        if (usuarioAtual != null) {
            usuarioAtual.adicionarPontuacao(pontuacaoPartida);
        }
    }
}
