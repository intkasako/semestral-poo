package entities;

public class Alternativa {
	private char letra;
    private String texto;
    private boolean veracidade;

    public Alternativa(String texto, char letra, boolean veracidade) {
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
}
