package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*") //o endereço do frontend seria posto aqui
public class PostagemController {
	//Facilita a manipulação do banco de dados diminuindo a quantidade de instâncias necessárias
	@Autowired
	private PostagemRepository postagemRepository;
	
	@Autowired
	private TemaRepository temaRepository;
	
	//Entidade de resposta HTTP (resposta para requisições):
	@GetMapping
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll()); //o mesmo que usar "SELECT * FROM tb_postagens;" no sgbd
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id){
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
		
		// SELECT * FROM tb_postagens WHERE id = ?;
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getAllByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
		// SELECT * FROM tb_postagens WHERE titulo LIKE "%?%";
	}
	
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
		if(temaRepository.existsById(postagem.getTema().getId())) {
			return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
			// o mesmo que INSERT INTO tb_postagens(titulo, texto) VALUES(?, ?);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este tema não existe.", null);
	}
	
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		
		if(postagemRepository.existsById(postagem.getId())) {
			if(temaRepository.existsById(postagem.getTema().getId())) {
				return ResponseEntity.ok(postagemRepository.save(postagem));
				// UPDATE tb_postagens SET titulo = ?, texto = ? WHERE id = ?;
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este tema não existe.", null);
		}
		return ResponseEntity.notFound().build();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
		Optional<Postagem> postagem = postagemRepository.findById(id);
		
		if(postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		postagemRepository.deleteById(id);
		
		// DELETE FROM tb_postagens WHERE id = ?;
	}
}