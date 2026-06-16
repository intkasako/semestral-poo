package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuizGameUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // --- Estado da tela de jogo ---
    private JLabel lblEnunciado;
    private JRadioButton[] opcoes;
    private ButtonGroup grupoAlternativas;
    private List<Questao> questoes;
    private int indiceAtual;

    // Modelo simples de questão usado pela tela de jogo
    private static class Questao {
        final String enunciado;
        final String[] alternativas;
        Questao(String enunciado, String[] alternativas) {
            this.enunciado = enunciado;
            this.alternativas = alternativas;
        }
    }

    private List<Questao> criarBancoDeQuestoes() {
        return List.of(
            new Questao("Qual é o paradigma principal do Java?",
                new String[]{"Funcional", "Orientação a Objetos", "Lógico", "Estruturado"}),
            new Questao("Qual palavra-chave cria uma subclasse em Java?",
                new String[]{"implements", "extends", "super", "new"}),
            new Questao("O que significa o 'O' de POO?",
                new String[]{"Operação", "Objeto", "Ordenação", "Otimização"}),
            new Questao("Qual estrutura permite repetir um bloco de código?",
                new String[]{"if", "switch", "for", "return"})
        );
    }

    public QuizGameUI() {
        setTitle("POO Quiz - Jogo Infinito");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // O CardLayout permite trocar as "telas" (painéis) facilmente
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inicializando as telas do jogo
        mainPanel.add(criarTelaLogin(), "TelaLogin");
        mainPanel.add(criarTelaConfiguracao(), "TelaConfiguracao");
        mainPanel.add(criarTelaJogo(), "TelaJogo");
        mainPanel.add(criarTelaLeaderboard(), "TelaLeaderboard");

        add(mainPanel);
    }

    // --- TELA 1: LOGIN DO USUÁRIO ---
    private JPanel criarTelaLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("Bem-vindo ao POO Quiz!");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        
        JLabel lblNome = new JLabel("Digite seu Nome:");
        JTextField txtNome = new JTextField(15);
        JButton btnEntrar = new JButton("Entrar"); //Botão para iniciar a interface.

        btnEntrar.addActionListener(e -> {
            if (!txtNome.getText().trim().isEmpty()) {
                cardLayout.show(mainPanel, "TelaConfiguracao"); //Caso tenha sido escrito um nome, levar à próxima tela.
            } else {
                JOptionPane.showMessageDialog(panel, "Por favor, insira um nome válido.");
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(lblNome, gbc);
        
        gbc.gridx = 1;
        panel.add(txtNome, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(btnEntrar, gbc);

        return panel;
    }

    // --- TELA 2: FILTROS (CATEGORIA E DIFICULDADE) ---
    private JPanel criarTelaConfiguracao() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblInstrucao = new JLabel("Configurar Partida");
        lblInstrucao.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel lblCategoria = new JLabel("Categoria (Assunto):");
        String[] categorias = {"Todos", "Matemática", "História", "Programação"};
        JComboBox<String> cbCategoria = new JComboBox<>(categorias);

        JLabel lblDificuldade = new JLabel("Dificuldade:");
        String[] dificuldades = {"Qualquer", "Fácil", "Médio", "Difícil"}; // Requer ajuste no Backend
        JComboBox<String> cbDificuldade = new JComboBox<>(dificuldades);

        JButton btnIniciar = new JButton("Iniciar Jogo!");
        
        btnIniciar.addActionListener(e -> {
            // Aqui você chamaria o GerenciadorQuiz para filtrar as questões
            cardLayout.show(mainPanel, "TelaJogo");
        });

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblInstrucao, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(lblCategoria, gbc);
        gbc.gridx = 1;
        panel.add(cbCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblDificuldade, gbc);
        gbc.gridx = 1;
        panel.add(cbDificuldade, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnIniciar, gbc);

        return panel;
    }

    // --- TELA 3: TELA DE JOGO (QUESTÃO) ---
    private JPanel criarTelaJogo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Banco de questões e estado inicial
        questoes = criarBancoDeQuestoes();
        indiceAtual = 0;

        // Atributo enunciado da classe Questao (preenchido dinamicamente)
        lblEnunciado = new JLabel();
        lblEnunciado.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(lblEnunciado, BorderLayout.NORTH);

        // Alternativas (preenchidas dinamicamente a cada questão)
        JPanel painelAlternativas = new JPanel(new GridLayout(4, 1, 5, 5));
        grupoAlternativas = new ButtonGroup();
        opcoes = new JRadioButton[4];
        for (int i = 0; i < opcoes.length; i++) {
            opcoes[i] = new JRadioButton();
            grupoAlternativas.add(opcoes[i]);
            painelAlternativas.add(opcoes[i]);
        }

        panel.add(painelAlternativas, BorderLayout.CENTER);

        // Carrega a primeira questão
        carregarQuestao(indiceAtual);

        // Botões de ação
        JPanel painelBotoes = new JPanel();
        JButton btnResponder = new JButton("Responder e Próxima");
        JButton btnSair = new JButton("Desistir e Ver Placar");

        btnResponder.addActionListener(e -> {
            if (grupoAlternativas.getSelection() == null) {
                JOptionPane.showMessageDialog(panel, "Selecione uma alternativa antes de responder.");
                return;
            }
            // Avança para a próxima questão (loop infinito pelo banco)
            indiceAtual = (indiceAtual + 1) % questoes.size();
            carregarQuestao(indiceAtual);
        });

        btnSair.addActionListener(e -> cardLayout.show(mainPanel, "TelaLeaderboard"));

        painelBotoes.add(btnResponder);
        painelBotoes.add(btnSair);
        panel.add(painelBotoes, BorderLayout.SOUTH);

        return panel;
    }

    // Atualiza o enunciado e as alternativas com a questão do índice informado
    private void carregarQuestao(int indice) {
        Questao q = questoes.get(indice);
        lblEnunciado.setText("<html>" + q.enunciado + "</html>");
        for (int i = 0; i < opcoes.length; i++) {
            opcoes[i].setText(q.alternativas[i]);
        }
        grupoAlternativas.clearSelection();
    }

    // --- TELA 4: PLACAR GLOBAL (LEADERBOARD) ---
    private JPanel criarTelaLeaderboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Placar Global", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Simulando o método exibirTabela() da classe Leaderboard
        String[] colunas = {"Posição", "Nome do Usuário", "Acertos"};
        Object[][] dados = {
            {"1", "Maria", "42"},
            {"2", "João", "38"},
            {"3", "Ana", "35"}
        };

        JTable tabelaPlacar = new JTable(dados, colunas);
        JScrollPane scrollPane = new JScrollPane(tabelaPlacar);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar ao Início");
        btnVoltar.addActionListener(e -> cardLayout.show(mainPanel, "TelaLogin"));
        panel.add(btnVoltar, BorderLayout.SOUTH);

        return panel;
    }

    // --- MAIN ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizGameUI().setVisible(true);
        });
    }
}