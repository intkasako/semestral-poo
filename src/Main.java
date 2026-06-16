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

        // deixa o jogador escolher os filtros antes de comecar
        List<Dificuldade> dificuldadesPermitidas = escolherDificuldades(scanner);
        List<String> categoriasPermitidas = escolherCategorias(scanner, questoes);

        // monta o gerenciador com as questoes ja filtradas por dificuldade e categoria
        GerenciadorQuiz gerenciador = new GerenciadorQuiz(
            questoes,
            usuario
        );

        System.out.println("=== Quiz infinito ===");
        System.out.println("O jogo acaba quando voce errar.\n");

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

        scanner.close();
    }

    private static Usuario loginOuCadastro(Scanner scanner, Leaderboard leaderboard) {
        System.out.println("=============================");
        System.out.println("   Bem-vindo ao POO Quiz!");
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

    private static List<String> escolherCategorias(Scanner scanner, List<Questao> questoes) {
        // Descobre todas as categorias únicas presentes nas questões carregadas
        List<String> categoriasDisponiveis = new ArrayList<>();
        for (Questao q : questoes) {
            if (!categoriasDisponiveis.contains(q.getAssunto())) {
                categoriasDisponiveis.add(q.getAssunto());
            }
        }

        System.out.println("\n=== Selecao de Categorias ===");
        System.out.println("Digite os numeros das categorias que deseja incluir separando por espaco.");
        System.out.println("Exemplo: 1 3 para incluir apenas as categorias 1 e 3.");
        
        for (int i = 0; i < categoriasDisponiveis.size(); i++) {
            System.out.println((i + 1) + " - " + categoriasDisponiveis.get(i));
        }
        int opcaoTodas = categoriasDisponiveis.size() + 1;
        System.out.println(opcaoTodas + " - Todas as categorias");
        System.out.print("Opcao: ");

        String escolha = scanner.nextLine();
        
        // Se escolheu "Todas" ou deixou em branco, retorna a lista completa
        if (escolha.contains(String.valueOf(opcaoTodas)) || escolha.trim().isEmpty()) {
            return categoriasDisponiveis;
        }

        List<String> categoriasSelecionadas = new ArrayList<>();
        String[] partes = escolha.split("\\s+"); // Separa por espaços
        
        for (String parte : partes) {
            try {
                int indice = Integer.parseInt(parte) - 1;
                if (indice >= 0 && indice < categoriasDisponiveis.size()) {
                    String cat = categoriasDisponiveis.get(indice);
                    if (!categoriasSelecionadas.contains(cat)) {
                        categoriasSelecionadas.add(cat);
                    }
                }
            } catch (NumberFormatException e) {
                // Ignora caso o usuário digite alguma letra inválida
            }
        }

        if (categoriasSelecionadas.isEmpty()) {
            System.out.println("Nenhuma categoria valida selecionada. Jogando com todas.");
            return categoriasDisponiveis;
        }

        return categoriasSelecionadas;
    }
}
