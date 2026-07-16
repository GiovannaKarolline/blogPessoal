package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*") //o endereço do frontend seria posto aqui
public class PostagemController {
	//Facilita a manipulação do banco de dados diminuindo a quantidade de instâncias necessárias
	@Autowired
	private PostagemRepository postagemRepository;
	
	//Entidade de resposta HTTP (resposta para requisições):
	@GetMapping
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll()); //o mesmo que usar "SELECT * FROM tb_postagens;" no sgbd
	}
}