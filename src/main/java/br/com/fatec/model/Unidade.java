package br.com.fatec.model;

public class Unidade {
    private int idUnidade;
    private String nome;
    private int cep;
    private String rua;
    private String bairro;
    private String cidade;
    private String uf;
    private String numero;

    // Construtores

    public Unidade() {
    }

    public Unidade(int idUnidade, String nome, int cep, String rua, String bairro, String cidade, String uf, String numero) {
        this.idUnidade = idUnidade;
        this.nome = nome;
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.numero = numero;
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

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    
}
