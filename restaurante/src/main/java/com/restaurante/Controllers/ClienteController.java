package com.restaurante.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.restaurante.model.Cliente;
import com.restaurante.repository.ClienteRepository;

import java.util.List;

@RestController 
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired 
    private ClienteRepository clienteRepository;

    // Endpoint para listar todos os clientes
    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll(); // Retorna todos os clientes do banco de dados
    }

    // Endpoint para buscar um cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        // Retorna o cliente se encontrado, ou um status 404 (não encontrado) se o ID não existir
        return clienteRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para criar um novo cliente
    @PostMapping
    public Cliente criar(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente); // Salva o novo cliente no banco de dados
    }

    // Endpoint para atualizar um cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente clienteDetalhes) {
        // Atualiza o cliente se encontrado, ou retorna status 404 se o ID não existir
        return clienteRepository.findById(id)
            .map(cliente -> {
                cliente.setNome(clienteDetalhes.getNome());
                cliente.setEmail(clienteDetalhes.getEmail());
                cliente.setTelefone(clienteDetalhes.getTelefone());
                return ResponseEntity.ok(clienteRepository.save(cliente)); // Salva as alterações
            }).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para deletar um cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        // Deleta o cliente se encontrado, ou retorna status 404 se o ID não existir
        return clienteRepository.findById(id)
            .map(cliente -> {
                clienteRepository.delete(cliente);
                return ResponseEntity.noContent().build(); // Retorna status 204 (sem conteúdo)
            }).orElse(ResponseEntity.notFound().build());
    }
}