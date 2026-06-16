package services;

import com.google.gson.*;
import entities.*;
import enums.Dificuldade;
import enums.Letra;

import java.io.*;
import java.util.*;

public class QuestaoParser {

    private Dificuldade mapearDificuldade(String difficulty) {
        return switch (difficulty) {
            case "easy"   -> Dificuldade.FACIL;
            case "medium" -> Dificuldade.MEDIO;
            case "hard"   -> Dificuldade.DIFICIL;
            default       -> Dificuldade.FACIL;
        };
    }

    public List<Questao> carregarQuestoes(String arquivo) throws IOException {
        List<Questao> questoes = new ArrayList<>();
        int id = 0;

        // lê o arquivo da pasta resources
        InputStream is = getClass().getClassLoader().getResourceAsStream(arquivo);
        String json = new String(is.readAllBytes());

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonArray results = root.getAsJsonArray("results");

        for (JsonElement el : results) {
            id++;
            JsonObject obj = el.getAsJsonObject();

            String type      = obj.get("type").getAsString();
            String enunciado = obj.get("question").getAsString();
            String assunto   = obj.get("category").getAsString();
            Dificuldade dif  = mapearDificuldade(obj.get("difficulty").getAsString());
            String correta   = obj.get("correct_answer").getAsString();

            if (type.equals("multiple")) {
                QuestaoMultiplaEscolha q = new QuestaoMultiplaEscolha(enunciado, assunto, id, dif);

                //monta lista de letras e embaralha
                List<Letra> letras = new ArrayList<>();
                for (Letra l : Letra.values()) {
                    letras.add(l);
                }
                Collections.shuffle(letras);

                //correta entra com a primeira letra sorteada
                q.addAlternativa(correta, letras.get(0), true);

                // incorretas entram com o restante das letras
                JsonArray incorretas = obj.getAsJsonArray("incorrect_answers");
                for (int i = 0; i < incorretas.size(); i++) {
                    q.addAlternativa(incorretas.get(i).getAsString(), letras.get(i + 1), false);
                }

                questoes.add(q);

            } else if (type.equals("boolean")) {
                char respostaCorreta = correta.charAt(0);
                questoes.add(new QuestaoVerdadeiroFalso(enunciado, assunto, id, respostaCorreta, dif));
            }
        }

        return questoes;
    }
}