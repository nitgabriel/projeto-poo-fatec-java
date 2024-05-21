package br.com.fatec.model;

public class Unidade {
    private int idUnidade;
    private String nome;
    private String endereco;

    // Construtores
    public Unidade() {}

    public Unidade(int idUnidade, String nome, String endereco) {
        this.idUnidade = idUnidade;
        this.nome = nome;
        this.endereco = endereco;
    }
    
    // Equals e HashCode

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.idUnidade;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Unidade other = (Unidade) obj;
        return this.idUnidade == other.idUnidade;
    }
    

    // Getters e Setters
    public int getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(int idUnidade) {
        this.idUnidade = idUnidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
