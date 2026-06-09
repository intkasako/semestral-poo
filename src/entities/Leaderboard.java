package entities;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    List<Usuario> ranking;

    public Leaderboard(List<Usuario> ranking) {
        this.ranking = new ArrayList<>();
    }
}
