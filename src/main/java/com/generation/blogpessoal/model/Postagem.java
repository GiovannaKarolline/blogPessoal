package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity //indica que é uma entidade
@Table(name = "tb_postagens") //define o nome dessa entidade na tabela do banco de dados
public class Postagem {
	@Id //Indica que o atributo seguinte é uma chave primária
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Id que se incrementa sozinho a cada registro
	private Long id;
	
	@NotBlank(message = "O atributo \"título\" é obrigatório!")
	@Size(min = 5, max = 100, message = "O atributo título deve ter, no mínimo, 5 caracteres e no máximo 100 caracteres.")
	@Column(length = 100) //adapta a coluna ao tamanho máximo aceito pelo atributo
	private String titulo;
	
	@NotBlank(message = "O atributo \"text\" é obrigatório!")
	@Size(min = 5, max = 1000, message = "O atributo texto deve ter, no mínimo, 5 caracteres e no máximo 100 caracteres.")
	@Column(length = 1000) //adapta a coluna ao tamanho máximo aceito pelo atributo
	private String texto;
	
	@UpdateTimestamp
	private LocalDateTime data;
	
	@ManyToOne //Postagens está do lado N da cardinalidade.
	//JsonIgnore evita que haja um looping infinito ao acessar as instâncias dos 
	//lados do relacionamento (seja de postagem ou tema).
	@JsonIgnoreProperties("postagem")
	private Tema tema;
	
	@JsonIgnoreProperties("postagem")
	private Usuario usuario;
	
	public Tema getTema() {
		return tema;
	}
	public void setTema(Tema tema) {
		this.tema = tema;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
}
