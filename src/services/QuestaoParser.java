package services;

import com.google.gson.*;
import entities.*;
import enums.Dificuldade;

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

}
