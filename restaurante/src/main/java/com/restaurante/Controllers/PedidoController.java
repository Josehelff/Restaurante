package com.restaurante.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.restaurante.model.Pedido;
import com.restaurante.repository.PedidoRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    
    @Autowired
    private PedidoRepository pedidoRepository;

    // Endpoint para listar todos os pedidos
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    // Endpoint para criar um novo pedido
    @PostMapping
    public Pedido criar(@RequestBody Pedido pedido) {
        // Define a data do pedido como o momento atual
        pedido.setDataPedido(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    // Endpoint para atualizar um pedido existente com base no ID
    @PutMapping("/{id}")
    public Pedido atualizar(@PathVariable Long id, @RequestBody Pedido pedidoAtualizado) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    // Atualiza os campos do pedido
                    pedido.setCliente(pedidoAtualizado.getCliente());
                    pedido.setCardapio(pedidoAtualizado.getCardapio());
                    pedido.setQuantidade(pedidoAtualizado.getQuantidade());
                    // Atualiza a data do pedido para o momento atual
                    pedido.setDataPedido(LocalDateTime.now());
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com o ID " + id));
    }

    // Endpoint para deletar um pedido com base no ID
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        pedidoRepository.deleteById(id);
    }
}
