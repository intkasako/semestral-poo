package enums;

public enum Dificuldade {
    FACIL(1, "Fácil"),
    MEDIO(2, "Médio"),
    DIFICIL(3, "Difícil"),
    GENIO(4, "Gênio");

    private final int valor;
    private final String descricao;

    Dificuldade(int valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public int getValor() { return valor; }
    public String getDescricao() { return descricao; }
}