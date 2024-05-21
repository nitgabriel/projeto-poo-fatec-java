package br.com.fatec.model;

import java.time.LocalDate;

public class Pet {
    private int idPet;
    private String nome;
    private String especie;
    private String numeroConveniado;
    private String raca;
    private LocalDate dataNascimento;
    private Dono dono;
    
    // Construtores

    public Pet(Dono dono) {
        this.dono = dono;
    }

    public Pet(int idPet, String nome, String especie, String numeroConveniado, String raca, LocalDate dataNascimento, Dono dono) {
        this.idPet = idPet;
        this.nome = nome;
        this.especie = especie;
        this.numeroConveniado = numeroConveniado;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.dono = dono;
    }
    
    // Equals e HashCode
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.idPet;
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
        final Pet other = (Pet) obj;
        return this.idPet == other.idPet;
    }
    
    // Getters e Setters
    public int getId() {
        return idPet;
    }

    public void setId(int idPet) {
        this.idPet = idPet;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNumeroConveniado() {
        return numeroConveniado;
    }

    public void setNumeroConveniado(String numeroConveniado) {
        this.numeroConveniado = numeroConveniado;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Dono getDono() {
        return dono;
    }

    public void setDono(Dono dono) {
        this.dono = dono;
    }
}
