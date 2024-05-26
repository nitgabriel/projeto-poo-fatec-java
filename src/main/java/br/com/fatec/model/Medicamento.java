package br.com.fatec.model;

import java.time.LocalDate;
import java.util.Objects;

public class Medicamento {
    private String nome;
    private String dosagem;
    private double preco;
    private LocalDate validade;
    private String descricao;

    // Construtores
    public Medicamento(String nome) {
        this.nome = nome;
    }

    public Medicamento(String nome, String dosagem, double preco, LocalDate validade, String descricao) {
        this.nome = nome;
        this.dosagem = dosagem;
        this.preco = preco;
        this.validade = validade;
        this.descricao = descricao;
    }

    /// Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicamento that = (Medicamento) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nome);
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Medicamento -  " + nome + '\'' +
                "\nDosagem:'" + dosagem + '\'' +
                "\nPreco:" + preco +
                "\nValidade:" + validade +
                "\nDescricao:'" + descricao + '\'';
    }
}
