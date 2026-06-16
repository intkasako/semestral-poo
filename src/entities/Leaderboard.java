package entities;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    List<Usuario> ranking;
    int index;

    public Leaderboard(List<Usuario> ranking) {
        this.ranking = new ArrayList<>();
    }
    
    public void processarLeaderboard(Usuario usuario) {
        
        usuario.isMaiorPontuacao();

        // verifica se já tem no ranking
    	if (usuario.existeLeaderboard() == false){
            ranking.add(usuario);
            usuario.setExisteLeaderboard(true);
        }

        // procura e salva o index da pessoa
        for (Usuario usuarios : ranking){
            if (usuarios.getId() == usuario.getId()){
                index = ranking.indexOf(usuarios);
            }
        }

        while (true){
            // Verifica se já é o primeiro lugar
            if (index == 0){
                break;
            }

            // Se a pessoa acima tiver mais pontos para o ciclo
            if (ranking.get(index).getpontuacaoMax() <= ranking.get(index-1).getpontuacaoMax()){
                break;
            }

            // Sobe no placar
            if (ranking.get(index).getpontuacaoMax() > ranking.get(index-1).getpontuacaoMax()){
                // Salva o usuario atual num placeholder
                Usuario temporario = ranking.get(index);
                // Seta o usuario atual como o acima dele
                ranking.set(index, ranking.get(index-1));
                // Seta o usuario antigamente acima como o usuario atual
                ranking.set(index-1, temporario);
                // "aumenta" o index em 1 pra subir pq 0 é o mais alto
                index -= 1;
            }
        }


    }
    
    
}
