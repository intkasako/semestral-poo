package entities;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private List<Integer> quizzesConcluidosID;

    public Usuario(int id, String nome, String email, List<Integer> quizzesConcluidosID) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.quizzesConcluidosID = new ArrayList<>();
    }

    public void addQuizzesConcluidosID(int id){
        this.quizzesConcluidosID.add(id);
    }

    public void mostrarQuizzesConcluidosID(){
        for (int a : quizzesConcluidosID){
            System.out.print(a);
        }
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
