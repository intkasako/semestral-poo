package gui;

import javax.swing.*;
import java.awt.*;

public class QuizGameUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

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

        // Simulando o atributo enunciado da classe Questao
        JLabel lblEnunciado = new JLabel("<html>Qual é o paradigma principal do Java?<html>");
        lblEnunciado.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(lblEnunciado, BorderLayout.NORTH);

        // Simulando QuestaoMultiplaEscolha
        JPanel painelAlternativas = new JPanel(new GridLayout(4, 1, 5, 5));
        ButtonGroup bg = new ButtonGroup();
        JRadioButton opt1 = new JRadioButton("Funcional");
        JRadioButton opt2 = new JRadioButton("Orientação a Objetos");
        JRadioButton opt3 = new JRadioButton("Lógico");
        JRadioButton opt4 = new JRadioButton("Estruturado");

        bg.add(opt1); bg.add(opt2); bg.add(opt3); bg.add(opt4);
        painelAlternativas.add(opt1); painelAlternativas.add(opt2);
        painelAlternativas.add(opt3); painelAlternativas.add(opt4);

        panel.add(painelAlternativas, BorderLayout.CENTER);

        // Botões de ação
        JPanel painelBotoes = new JPanel();
        JButton btnResponder = new JButton("Responder e Próxima");
        JButton btnSair = new JButton("Desistir e Ver Placar");

        btnResponder.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel, "Resposta registrada! Carregando próxima questão...");
            // Lógica de loop infinito de questões entraria aqui
            bg.clearSelection();
        });

        btnSair.addActionListener(e -> cardLayout.show(mainPanel, "TelaLeaderboard"));

        painelBotoes.add(btnResponder);
        painelBotoes.add(btnSair);
        panel.add(painelBotoes, BorderLayout.SOUTH);

        return panel;
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