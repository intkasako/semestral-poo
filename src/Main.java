import entities.Questao;
import entities.Usuario;
import enums.Dificuldade;
import services.GerenciadorQuiz;
import services.QuestaoParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Usuario usuario = new Usuario(1, "Pedro", "pedro@email.com");
        List<Questao> questoes = carregarQuestoes();

        if (questoes.isEmpty()) {
            System.out.println("Nenhuma questao foi carregada. Encerrando teste.");
            scanner.close();
            return;
        }

        List<Dificuldade> dificuldadesPermitidas = escolherDificuldades(scanner);

        GerenciadorQuiz gerenciador = new GerenciadorQuiz(
            questoes,
            usuario,
            dificuldadesPermitidas
        );

        System.out.println("=== Quiz infinito ===");
        System.out.println("O jogo acaba quando voce errar.\n");

        while (!gerenciador.isJogoFinalizado()) {
            Questao questaoAtual = gerenciador.getQuestaoAtual();

            System.out.println("ID: " + questaoAtual.getId());
            System.out.println("Dificuldade: " + questaoAtual.getDificuldade().getDescricao());
            System.out.println("Pergunta: " + questaoAtual.getEnunciado());
            System.out.print("Resposta: ");

            String resposta = scanner.nextLine();

            boolean acertou = gerenciador.responder(resposta);

            if (acertou) {
                System.out.println("Acertou!");
                System.out.println("Pontuacao da partida: " + gerenciador.getPontuacaoAtual());
                System.out.println("Acertos: " + gerenciador.getAcertos());
                System.out.println("IDs ja sorteados: " + gerenciador.getIdsQuestoesSorteadas());
                System.out.println();
            } else {
                System.out.println("Errou! Fim de jogo.");
            }
        }

        System.out.println("\nPontuacao final da partida: " + gerenciador.getPontuacaoAtual());
        System.out.println("Pontuacao global do usuario: " + usuario.getPontuacao());
        System.out.println("Total de acertos: " + gerenciador.getAcertos());

        scanner.close();
    }

    private static List<Questao> carregarQuestoes() {
        QuestaoParser parser = new QuestaoParser();

        try {
            return parser.carregarQuestoes("testequestoes.txt");
        } catch (IOException e) {
            System.out.println("Nao foi possivel carregar o arquivo de questoes.");
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    private static List<Dificuldade> escolherDificuldades(Scanner scanner) {
        List<Dificuldade> dificuldadesPermitidas = new ArrayList<>();

        System.out.println("=== Selecao de dificuldades ===");
        System.out.println("Digite os numeros das dificuldades que deseja liberar.");
        System.out.println("Exemplo: 1 2 para liberar faceis e medias.");
        System.out.println("1 - Faceis");
        System.out.println("2 - Medias");
        System.out.println("3 - Dificeis");
        System.out.println("4 - Todas");
        System.out.print("Opcao: ");

        String escolha = scanner.nextLine();

        if (escolha.contains("4")) {
            return List.of(Dificuldade.FACIL, Dificuldade.MEDIO, Dificuldade.DIFICIL);
        }

        if (escolha.contains("1")) {
            dificuldadesPermitidas.add(Dificuldade.FACIL);
        }

        if (escolha.contains("2")) {
            dificuldadesPermitidas.add(Dificuldade.MEDIO);
        }

        if (escolha.contains("3")) {
            dificuldadesPermitidas.add(Dificuldade.DIFICIL);
        }

        if (dificuldadesPermitidas.isEmpty()) {
            System.out.println("Nenhuma dificuldade valida selecionada. Usando todas.\n");
            return List.of(Dificuldade.FACIL, Dificuldade.MEDIO, Dificuldade.DIFICIL);
        }

        System.out.println();
        return dificuldadesPermitidas;
    }
}
