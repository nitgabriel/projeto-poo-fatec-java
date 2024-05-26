package br.com.fatec.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Agendamento {
    private int idAgendamento;
    private Dono dono;
    private Pet pet;
    private Veterinario veterinario;
    private Unidade unidade;
    private String especialidade;
    private LocalDate data;
    private LocalTime horario;

    // Construtores
    public Agendamento(Dono dono, Pet pet, Veterinario veterinario, Unidade unidade) {
        this.dono = dono;
        this.pet = pet;
        this.veterinario = veterinario;
        this.unidade = unidade;
    }

    public Agendamento(int idAgendamento, Dono dono, Pet pet, Veterinario veterinario, Unidade unidade, String especialidade, LocalDate data, LocalTime horario) {
        this.idAgendamento = idAgendamento;
        this.dono = dono;
        this.pet = pet;
        this.veterinario = veterinario;
        this.unidade = unidade;
        this.especialidade = especialidade;
        this.data = data;
        this.horario = horario;
    }
    
    // Equals e HashCode

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.idAgendamento;
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
        final Agendamento other = (Agendamento) obj;
        return this.idAgendamento == other.idAgendamento;
    }
    

    // Getters e Setters
    public int getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(int idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public Dono getDono() {
        return dono;
    }

    public void setDono(Dono dono) {
        this.dono = dono;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return idAgendamento + " " + data +
                " " + horario + " " +
                pet;
    }
}
