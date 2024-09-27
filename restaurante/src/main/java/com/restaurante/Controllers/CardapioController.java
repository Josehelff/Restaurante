package com.restaurante.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.restaurante.model.Cardapio;
import com.restaurante.repository.CardapioRepository;

import java.util.List;

@RestController 
@RequestMapping("/cardapios") 
public class CardapioController {

    @Autowired 
    private CardapioRepository cardapioRepository;

    // Endpoint para listar todos os itens do cardápio
    @GetMapping
    public List<Cardapio> listarTodos() {
        return cardapioRepository.findAll(); // Retorna todos os itens do cardápio do banco de dados
    }

    // Endpoint para buscar um item do cardápio por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cardapio> buscarPorId(@PathVariable Long id) {
        return cardapioRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para criar um novo item no cardápio
    @PostMapping
    public ResponseEntity<Cardapio> criar(@RequestBody Cardapio cardapio) {
        Cardapio novoCardapio = cardapioRepository.save(cardapio);
        return ResponseEntity.ok(novoCardapio); // Retorna o novo item salvo no banco de dados
    }

    // Endpoint para atualizar um item do cardápio existente
    @PutMapping("/{id}")
    public ResponseEntity<Cardapio> atualizar(@PathVariable Long id, @RequestBody Cardapio cardapioDetalhes) {
        return cardapioRepository.findById(id)
            .map(cardapio -> {
                cardapio.setNomePrato(cardapioDetalhes.getNomePrato());
                cardapio.setDescricao(cardapioDetalhes.getDescricao());
                cardapio.setPreco(cardapioDetalhes.getPreco());
                Cardapio atualizado = cardapioRepository.save(cardapio);
                return ResponseEntity.ok(atualizado); // Salva as alterações
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para deletar um item do cardápio
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return cardapioRepository.findById(id)
            .map(cardapio -> {
                cardapioRepository.delete(cardapio);
                return ResponseEntity.noContent().build(); // Retorna 204 No Content
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
