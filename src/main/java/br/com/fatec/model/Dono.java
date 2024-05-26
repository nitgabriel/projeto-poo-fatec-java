package br.com.fatec.model;

public class Dono {
    private int idDono;
    private String nome;
    private String email;
    private String cpf;
    private String formaPagamento;
    private String contato;
    
    // Construtores

    public Dono() {}

    public Dono(int idDono, String nome, String email, String cpf, String formaPagamento, String contato) {
        this.idDono = idDono;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.formaPagamento = formaPagamento;
        this.contato = contato;
    }
    
    // Equals e HashCode
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.idDono;
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
        final Dono other = (Dono) obj;
        return this.idDono == other.idDono;
    }

    
    // Getters e Setters
    public int getIdDono() {
        return idDono;
    }

    public void setIdDono(int idDono) {
        this.idDono = idDono;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    @Override
    public String toString() {
        return "id " + idDono +
                " " + nome;
    }
}
