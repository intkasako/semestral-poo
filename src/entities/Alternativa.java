package entities;

import enums.Letra;

public class Alternativa {
	private Letra letra;
    private String texto;
    private boolean veracidade;

    public Alternativa(String texto, Letra letra, boolean veracidade) {
        this.veracidade = veracidade;
        this.texto = texto;
        this.letra = letra;
    }

    public String getTexto() {
        return texto;
    }

    /*public void setTexto(String texto) {
        this.texto = texto;
    }*/

    public boolean isVeracidade() {
        return veracidade;
    }

    /*public void setVeracidade(boolean veracidade) {
        this.veracidade = veracidade;
    }*/

    public Letra getLetra() {
        return letra;
    }
}
