package com.generation.blogpessoal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_temas")
public class Tema {
	@Id //Indica que o atributo seguinte é uma chave primária
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Id que se incrementa sozinho a cada registro
	private Long id;
	
	@NotBlank(message = "O atributo \"descrição\" é obrigatório!")
	@Size(min = 5, max = 1200, message = "O atributo \"descrição\" deve ter, no mínimo, 5 caracteres e no máximo 100 caracteres.")
	@Column(length = 1200) //adapta a coluna ao tamanho máximo aceito pelo atributo
	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}