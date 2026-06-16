package services;

import entities.Questao;
import entities.Usuario;
import enums.Dificuldade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//Esta classe controla o fluxo do jogo a partir do inicio da partida ate o fim.
public class GerenciadorQuiz {
    private List<Questao> questoes;
    private Map<Dificuldade, List<Questao>> questoesPorDificuldade; //Map de dificuldades, por List de Questao. A List de Questao tem as questões daquela dificuldade que ainda não foram sorteadas.
    private Map<Dificuldade, Integer> indicesPorDificuldade; //Map de dificuldades, por int. O int é o index da próxima questão daquela dificuldade a ser sorteada.
    private Usuario usuarioAtual;
    private Questao questaoAtual;
    private Boolean jogoFinalizado;
    private Random random;
    private Integer acertos;
    private Integer pontuacaoPartida;
    private List<Dificuldade> dificuldadesPermitidas;
    private List<Integer> idsQuestoesSorteadas;
    private List<String> categoriasPermitidas;

    public GerenciadorQuiz(List<Questao> questoes, Usuario usuarioAtual, List<Dificuldade> dificuldadesPermitidas, List<String> categoriasPermitidas) {
        this.questoes = questoes;
        this.questoesPorDificuldade = new EnumMap<>(Dificuldade.class);
        this.indicesPorDificuldade = new EnumMap<>(Dificuldade.class);
        this.usuarioAtual = usuarioAtual;
        this.questaoAtual = null;
        this.jogoFinalizado = false;
        this.random = new Random();
        this.acertos = 0;
        this.pontuacaoPartida = 0;
        this.dificuldadesPermitidas = dificuldadesPermitidas;
        this.idsQuestoesSorteadas = new ArrayList<>();
        this.categoriasPermitidas = categoriasPermitidas;

        organizarQuestoesPorDificuldade();
        sortearProximaQuestao();
    }

    // Getters e Setters:

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
        this.idsQuestoesSorteadas.clear();
        organizarQuestoesPorDificuldade();
        sortearProximaQuestao();
    }

    public Usuario getUsuarioAtual() {
        return usuarioAtual;
    }

    public void setUsuarioAtual(Usuario usuarioAtual) {
        this.usuarioAtual = usuarioAtual;
    }

    public Questao getQuestaoAtual() {
        return questaoAtual;
    }

    public void setQuestaoAtual(Questao questaoAtual) {
        this.questaoAtual = questaoAtual;
    }

    public Boolean getJogoFinalizado() {
        return jogoFinalizado;
    }

    public void setJogoFinalizado(Boolean jogoFinalizado) {
        this.jogoFinalizado = jogoFinalizado;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Integer getAcertos() {
        return acertos;
    }

    public void setAcertos(Integer acertos) {
        this.acertos = acertos;
    }

    public Integer getPontuacaoPartida() {
        return pontuacaoPartida;
    }

    public void setPontuacaoPartida(Integer pontuacaoPartida) {
        this.pontuacaoPartida = pontuacaoPartida;
    }

    public List<Dificuldade> getDificuldadesPermitidas() {
        return dificuldadesPermitidas;
    }

    public void setDificuldadesPermitidas(List<Dificuldade> dificuldadesPermitidas) {
        this.dificuldadesPermitidas = dificuldadesPermitidas;
        sortearProximaQuestao();
    }

    public List<Integer> getIdsQuestoesSorteadas() {
        return idsQuestoesSorteadas;
    }

    public List<Integer> getIdsQuestaoSorteada() {
        return getIdsQuestoesSorteadas();
    }

    public List<String> getCategoriasPermitidas() {
        return categoriasPermitidas;
    }

    public void setCategoriasPermitidas(List<String> categoriasPermitidas) {
        this.categoriasPermitidas = categoriasPermitidas;
        sortearProximaQuestao();
    }

    // Funcoes:

    public Integer getPontuacaoAtual() {
        return pontuacaoPartida;
    }

    public Boolean isJogoFinalizado() {
        return jogoFinalizado;
    }

    private void organizarQuestoesPorDificuldade() {
        questoesPorDificuldade.clear();
        indicesPorDificuldade.clear();

        for (Dificuldade dificuldade : Dificuldade.values()) {
            questoesPorDificuldade.put(dificuldade, new ArrayList<>());
            indicesPorDificuldade.put(dificuldade, 0);
        }

        if (questoes == null) {
            return;
        }

        for (Questao questao : questoes) {
            if (categoriaPermitida(questao.getAssunto())) { //Se a categoria da questão for permitida, ela pegará as questões por categoria.
                questoesPorDificuldade.get(questao.getDificuldade()).add(questao); //Se a dificuldade da questão for permitida, ela pegará as questões por dificuldade.
            }
        }

        for (List<Questao> lista : questoesPorDificuldade.values()) {
            Collections.shuffle(lista, random);
        }
    }

    private Dificuldade sortearDificuldadePorProgresso() { //Determina dificuldade do jogo por parte onde usuário está, ou seja, quantas questões ele já acertou. Usa de porcentagem.
        if (acertos < 5) {
            return Dificuldade.FACIL;
        }

        int chance = random.nextInt(100);

        if (acertos < 15) {
            int chanceMedia = (acertos - 4) * 10;
            return chance < chanceMedia ? Dificuldade.MEDIO : Dificuldade.FACIL;
        }

        if (acertos < 30) {
            if (chance < 30) {
                return Dificuldade.FACIL;
            }

            if (chance < 75) {
                return Dificuldade.MEDIO;
            }

            return Dificuldade.DIFICIL;
        }

        if (acertos < 50) {
            int chanceDificil = 40 + ((acertos - 30) * 3);
            return chance < chanceDificil ? Dificuldade.DIFICIL : Dificuldade.MEDIO;
        }

        return Dificuldade.DIFICIL; //A partir dos 50 acertos, só questões difíceis aparecem.
    }

    private Dificuldade sortearDificuldadePermitida() {
        if (dificuldadesPermitidas == null || dificuldadesPermitidas.isEmpty()) {
            return sortearDificuldadePorProgresso();
        }

        for (int tentativa = 0; tentativa < 10; tentativa++) {
            Dificuldade dificuldadeSorteada = sortearDificuldadePorProgresso();

            if (dificuldadesPermitidas.contains(dificuldadeSorteada)) {
                return dificuldadeSorteada;
            }
        }

        int indiceSorteado = random.nextInt(dificuldadesPermitidas.size());
        return dificuldadesPermitidas.get(indiceSorteado);
    }

    private void sortearProximaQuestao() { //Sorteia a próxima questão a ser respondida, levando em conta as dificuldades e categorias permitidas, e garantindo que a mesma questão não seja sorteada mais de uma vez.

        //Verifica se há questão carregada no jogo (Se não, para).
        if (questoes == null || questoes.isEmpty()) {
            questaoAtual = null;
            return;
        }

        //Sorteia a dificuldade da próxima questão.
        Dificuldade dificuldadeSorteada = sortearDificuldadePermitida();
        
        //Busca a questão em si.
        Questao questaoSorteada = pegarProximaQuestaoDaDificuldade(dificuldadeSorteada);

        if (questaoSorteada == null) {
            questaoSorteada = pegarProximaQuestaoPermitida();
        }

        questaoAtual = questaoSorteada;
        
        //Guarda o ID da questão numa lista para manter um histórico das perguntas que já saíram nesta partida.
        if (questaoAtual != null) {
            idsQuestoesSorteadas.add(questaoAtual.getId());
        }
    }

    private Questao pegarProximaQuestaoDaDificuldade(Dificuldade dificuldade) {

        //Se o jogador não permitiu essa dificuldade no menu inicial, aborta e retorna null.
        if (!dificuldadePermitida(dificuldade)) {
            return null;
        }

        //Pega a lista de questões correspondente àquela dificuldade.
        List<Questao> lista = questoesPorDificuldade.get(dificuldade);

        if (lista == null || lista.isEmpty()) {
            return null;
        }

        Integer indiceAtual = indicesPorDificuldade.get(dificuldade);

        if (indiceAtual >= lista.size()) {
            Collections.shuffle(lista, random);
            indiceAtual = 0;
        }

        Questao questao = lista.get(indiceAtual);
        indicesPorDificuldade.put(dificuldade, indiceAtual + 1);
        return questao;
    }

    private Questao pegarProximaQuestaoPermitida() {
        List<Dificuldade> dificuldadesComQuestoes = new ArrayList<>();

        for (Dificuldade dificuldade : Dificuldade.values()) {
            List<Questao> lista = questoesPorDificuldade.get(dificuldade);

            if (dificuldadePermitida(dificuldade) && lista != null && !lista.isEmpty()) {
                dificuldadesComQuestoes.add(dificuldade);
            }
        }

        if (dificuldadesComQuestoes.isEmpty()) {
            return null;
        }

        Dificuldade dificuldadeSorteada = dificuldadesComQuestoes.get(random.nextInt(dificuldadesComQuestoes.size()));
        return pegarProximaQuestaoDaDificuldade(dificuldadeSorteada);
    }

    private boolean dificuldadePermitida(Dificuldade dificuldade) {
        return dificuldadesPermitidas == null || dificuldadesPermitidas.isEmpty() || dificuldadesPermitidas.contains(dificuldade);
    }

    private boolean categoriaPermitida(String categoria) {
        return categoriasPermitidas == null || categoriasPermitidas.isEmpty() || categoriasPermitidas.contains(categoria);
    }

    public boolean responder(String respostaDoUsuario) {
        Questao questaoAtual = getQuestaoAtual();

        if (questaoAtual == null || jogoFinalizado) {
            return false;
        }

        boolean acertou = questaoAtual.verificarResposta(respostaDoUsuario);

        if (acertou) {
            pontuacaoPartida += questaoAtual.calcularPontuacao();
            acertos++;
            sortearProximaQuestao();
        } else {
            jogoFinalizado = true;
            somarPontuacaoPartidaAoUsuario();
        }

        return acertou;
    }

    private void somarPontuacaoPartidaAoUsuario() {
        usuarioAtual.adicionarPontuacao(pontuacaoPartida);
    }
}
