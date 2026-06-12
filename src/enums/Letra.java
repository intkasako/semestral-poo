package enums;

public enum Letra {
    A(1, 'A'),
    B(2, 'B'),
    C(3, 'C'),
    D(4, 'D');

    private int indice;
    private char simbolo;

    Letra(int indice, char simbolo){
        this.indice = indice;
        this.simbolo = simbolo;
    }

    public int getValor() {
        return indice;
    }
    public char getSimbolo() {
        return simbolo;
    }
}
