package entities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    private List<Usuario> ranking;
    private String arquivo;

    // ao criar o leaderboard, ja tenta carregar os dados salvos do arquivo
    public Leaderboard(String arquivo) {
        this.ranking = new ArrayList<>();
        this.arquivo = arquivo;
        carregar();
    }

    // atualiza o recorde do jogador, adiciona ele no ranking se for novo, reordena e salva no txt
    public void registrar(Usuario usuario) {
        usuario.isMaiorPontuacao();

        // verifica se ja tem no ranking
        if (!usuario.existeLeaderboard()) {
            ranking.add(usuario);
            usuario.setExisteLeaderboard(true);
        }

        // procura e salva o index da pessoa
        int index = 0;
        for (Usuario u : ranking) {
            if (u.getId() == usuario.getId()) {
                index = ranking.indexOf(u);
            }
        }

        while (true) {
            // verifica se ja e o primeiro lugar
            if (index == 0) {
                break;
            }

            // se a pessoa acima tiver mais pontos, para o ciclo
            if (ranking.get(index).getpontuacaoMax() <= ranking.get(index - 1).getpontuacaoMax()) {
                break;
            }

            // sobe no placar
            if (ranking.get(index).getpontuacaoMax() > ranking.get(index - 1).getpontuacaoMax()) {
                Usuario temporario = ranking.get(index);
                ranking.set(index, ranking.get(index - 1));
                ranking.set(index - 1, temporario);
                index -= 1;
            }
        }

        salvar();
    }

    // procura um usuario pelo nome e email pra fazer o login
    public Usuario buscarUsuario(String nome, String email) {
        for (Usuario u : ranking) {
            if (u.getNome().equalsIgnoreCase(nome) && u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    // pega o maior id que existe e soma 1, assim cada usuario novo tem um id unico
    public int proximoId() {
        int maior = 0;
        for (Usuario u : ranking) {
            if (u.getId() > maior) {
                maior = u.getId();
            }
        }
        return maior + 1;
    }

    // mostra o placar no console, do primeiro ao ultimo lugar
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

    // escreve todos os usuarios no txt, uma linha por jogador no formato: id;nome;email;pontuacaoMax
    private void salvar() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
            for (Usuario u : ranking) {
                writer.println(u.getId() + ";" + u.getNome() + ";" + u.getEmail() + ";" + u.getpontuacaoMax());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar leaderboard: " + e.getMessage());
        }
    }

    // le o txt linha por linha e recria cada usuario com seus dados salvos
    private void carregar() {
        File file = new File(arquivo);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                // cada linha tem o formato: id;nome;email;pontuacaoMax
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

        // ordena do maior pro menor usando a mesma logica do registrar
        for (int i = 1; i < ranking.size(); i++) {
            int index = i;
            while (true) {
                if (index == 0) break;
                if (ranking.get(index).getpontuacaoMax() <= ranking.get(index - 1).getpontuacaoMax()) break;

                Usuario temporario = ranking.get(index);
                ranking.set(index, ranking.get(index - 1));
                ranking.set(index - 1, temporario);
                index -= 1;
            }
        }
    }

    public List<Usuario> getRanking() {
        return ranking;
    }

    // remove o usuario do ranking (pelo id) e salva o txt atualizado
    public boolean deletar(Usuario usuario) {
        boolean removido = ranking.removeIf(u -> u.getId() == usuario.getId());

        if (removido) {
            usuario.setExisteLeaderboard(false);
            salvar();
        }

        return removido;
    }


}
