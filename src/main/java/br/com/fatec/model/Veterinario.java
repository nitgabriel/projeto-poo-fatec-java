package br.com.fatec.model;

public class Veterinario {
    private int idVeterinario;
    private String nome;
    private String crmv;
    private Unidade unidade;
    private String status;
    private String especialidade;

    // Construtores
    public Veterinario(Unidade unidade) {
        this.unidade = unidade;
    }

    public Veterinario(int idVeterinario, String nome, String crmv, Unidade unidade, String status, String especialidade) {
        this.idVeterinario = idVeterinario;
        this.nome = nome;
        this.crmv = crmv;
        this.unidade = unidade;
        this.status = status;
        this.especialidade = especialidade;
    }
    
    // Equals e HashCode

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.idVeterinario;
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
        final Veterinario other = (Veterinario) obj;
        return this.idVeterinario == other.idVeterinario;
    }

    // Getters e Setters
    public int getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return idVeterinario +
                " - " + nome +
                " crmv=" + crmv;
    }
}
