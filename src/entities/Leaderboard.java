package entities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    private List<Usuario> ranking;
    private String arquivo;

    public Leaderboard(String arquivo) {
        this.ranking = new ArrayList<>();
        this.arquivo = arquivo;
        carregar();
    }

    public void registrar(Usuario usuario) {
        usuario.isMaiorPontuacao();

        if (!usuario.existeLeaderboard()) {
            ranking.add(usuario);
            usuario.setExisteLeaderboard(true);
        }

        ranking.sort((a, b) -> b.getpontuacaoMax() - a.getpontuacaoMax());
        salvar();
    }

    public Usuario buscarUsuario(String nome, String email) {
        for (Usuario u : ranking) {
            if (u.getNome().equalsIgnoreCase(nome) && u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public int proximoId() {
        int maior = 0;
        for (Usuario u : ranking) {
            if (u.getId() > maior) {
                maior = u.getId();
            }
        }
        return maior + 1;
    }

    public void exibir() {
        System.out.println();
        System.out.println("=============================");
        System.out.println("       LEADERBOARD");
        System.out.println("=============================");

        if (ranking.isEmpty()) {
            System.out.println("Nenhum jogador registrado.");
        } else {
            for (int i = 0; i < ranking.size(); i++) {
                Usuario u = ranking.get(i);
                System.out.println("  " + (i + 1) + "o - " + u.getNome() + " | " + u.getpontuacaoMax() + " pts");
            }
        }

        System.out.println("=============================");
    }

    private void salvar() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
            for (Usuario u : ranking) {
                writer.println(u.getId() + ";" + u.getNome() + ";" + u.getEmail() + ";" + u.getpontuacaoMax());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar leaderboard: " + e.getMessage());
        }
    }

    private void carregar() {
        File file = new File(arquivo);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] partes = linha.split(";");
                int id = Integer.parseInt(partes[0]);
                String nome = partes[1];
                String email = partes[2];
                int pontuacaoMax = Integer.parseInt(partes[3]);

                Usuario u = new Usuario(id, nome, email);
                u.setPontuacaoMax(pontuacaoMax);
                u.setExisteLeaderboard(true);
                ranking.add(u);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar leaderboard: " + e.getMessage());
        }

        ranking.sort((a, b) -> b.getpontuacaoMax() - a.getpontuacaoMax());
    }

    public List<Usuario> getRanking() {
        return ranking;
    }
}
