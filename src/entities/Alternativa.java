package entities;

public class Alternativa {
    private String texto;
    private boolean veracidade;

    public Alternativa(String texto, boolean veracidade) {
        this.veracidade = veracidade;
        this.texto = texto;
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
