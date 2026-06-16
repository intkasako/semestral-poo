import entities.Alternativa;
import entities.Leaderboard;
import entities.Questao;
import entities.QuestaoMultiplaEscolha;
import entities.QuestaoVerdadeiroFalso;
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

        // carrega o ranking salvo em arquivo e faz login ou cadastro do jogador
        Leaderboard leaderboard = new Leaderboard("leaderboard.txt");
        Usuario usuario = loginOuCadastro(scanner, leaderboard);
        List<Questao> questoes = carregarQuestoes();

        if (questoes.isEmpty()) {
            System.out.println("Nenhuma questao foi carregada. Encerrando teste.");
            scanner.close();
            return;
        }

        Boolean continuarJogando = true;

        while (continuarJogando) {

            // monta o gerenciador com as questoes ja filtradas por dificuldade e categoria
            GerenciadorQuiz gerenciador = new GerenciadorQuiz(
                questoes,
                usuario
            );

            System.out.println("=== Quiz infinito ===");
            System.out.println("O jogo só acabará quando voce errar.\n");

            // loop principal — continua ate o jogador errar
            while (!gerenciador.isJogoFinalizado()) {
                Questao questaoAtual = gerenciador.getQuestaoAtual();

                System.out.println("ID: " + questaoAtual.getId());
                System.out.println("Dificuldade: " + questaoAtual.getDificuldade().getDescricao());
                System.out.println("Pergunta: " + questaoAtual.getEnunciado());

                // mostra as opcoes dependendo do tipo da questao
                if (questaoAtual instanceof QuestaoMultiplaEscolha qme) {
                    // ordena pra sempre mostrar A, B, C, D em ordem
                    List<Alternativa> ordenadas = new ArrayList<>(qme.getAlternativas());
                    ordenadas.sort((a, b) -> a.getLetra().getValor() - b.getLetra().getValor());
                    for (Alternativa alt : ordenadas) {
                        System.out.println("  " + alt.getLetra().getSimbolo() + ") " + alt.getTexto());
                    }
                } else if (questaoAtual instanceof QuestaoVerdadeiroFalso) {
                    System.out.println("  V) Verdadeiro");
                    System.out.println("  F) Falso");
                }

                System.out.print("Resposta: ");

                String resposta = scanner.nextLine();

                boolean acertou = gerenciador.responder(resposta);

                if (acertou) {
                    System.out.println("Acertou!");
                    System.out.println("Pontuacao da partida: " + gerenciador.getPontuacaoAtual());
                    System.out.println("Acertos: " + gerenciador.getAcertos());
                    System.out.println();
                } else {
                    System.out.println("Errou! Fim de jogo.");
                }
            }

            // resumo final da partida
            System.out.println("\nPontuacao final da partida: " + gerenciador.getPontuacaoAtual());
            System.out.println("Pontuacao global de " + usuario.getNome() + ": " + usuario.getPontuacao());
            System.out.println("Total de acertos: " + gerenciador.getAcertos());

            // salva o resultado no ranking e mostra o placar
            leaderboard.registrar(usuario);
            leaderboard.exibir();

            // OPÇÃO DE SAÍDA
            System.out.println("\nDeseja jogar novamente? (S/N)");
            System.out.print("Opcao: ");
            String escolhaDeSaida = scanner.nextLine().trim().toUpperCase();

            if (escolhaDeSaida.equals("N")) {
                continuarJogando = false; // Isso quebra o loop principal e finaliza o programa
                System.out.println("\nObrigado por jogar, " + usuario.getNome() + "! Ate logo.");
            }
        } // Fim do loop principal

        scanner.close();
    }

    private static Usuario loginOuCadastro(Scanner scanner, Leaderboard leaderboard) {
        System.out.println("=============================");
        System.out.println("   Bem-vindo ao nosso jogo de Quiz!");
        System.out.println("=============================");
        System.out.println();
        System.out.println("1 - Entrar");
        System.out.println("2 - Cadastrar");
        System.out.print("Opcao: ");

        String opcao = scanner.nextLine().trim();

        System.out.println();

        String nome = "";
        while (nome.isEmpty()) {
            System.out.print("Digite seu nome: ");
            nome = scanner.nextLine().trim();
            if (nome.isEmpty()) {
                System.out.println("Nome nao pode ser vazio.");
            }
        }

        String email = "";
        while (email.isEmpty()) {
            System.out.print("Digite seu e-mail: ");
            email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("E-mail nao pode ser vazio.");
            }
        }

        if (opcao.equals("1")) {
            Usuario encontrado = leaderboard.buscarUsuario(nome, email);
            if (encontrado != null) {
                System.out.println();
                System.out.println("Bem-vindo de volta, " + encontrado.getNome() + "!");
                System.out.println("Sua maior pontuacao: " + encontrado.getpontuacaoMax() + " pts");
                System.out.println();
                return encontrado;
            } else {
                System.out.println("Usuario nao encontrado. Criando conta nova...");
            }
        }

        Usuario novo = new Usuario(leaderboard.proximoId(), nome, email);
        System.out.println();
        System.out.println("Cadastro realizado com sucesso!");
        System.out.println("Jogador: " + nome);
        System.out.println("E-mail: " + email);
        System.out.println();
        return novo;
    }

    private static List<Questao> carregarQuestoes() {
        QuestaoParser parser = new QuestaoParser();

        try {
            return parser.carregarQuestoes("resources/testequestoes.txt");
        } catch (IOException e) {
            System.out.println("Nao foi possivel carregar o arquivo de questoes.");
            System.out.println(e.getMessage());
            return List.of();
        }
    }
}
