package com.restaurante.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.List;



import jakarta.persistence.CascadeType;

@Entity
public class Cardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cardapio_seq")
    @SequenceGenerator(name = "cardapio_seq", sequenceName = "cardapio_sequence", allocationSize = 1)
    private Long cardapioId;

    private String nomePrato;
    private String descricao;
    private Double preco;

    @OneToMany(mappedBy = "cardapio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos;

   

    public Long getCardapioId() {
        return cardapioId;
    }

    public void setCardapioId(Long cardapioId) {
        this.cardapioId = cardapioId;
    }

    public String getNomePrato() {
        return nomePrato;
    }

    public void setNomePrato(String nomePrato) {
        this.nomePrato = nomePrato;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
