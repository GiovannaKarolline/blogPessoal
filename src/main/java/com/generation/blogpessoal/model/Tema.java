package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	
	// Dados sendo carregados a partir da primeira consulta neles (LAZY) e não mantidos preventivamente, deixa a aplicação mais leve
	// Cascade do tipo REMOVE faz com que todas as postagens associadas ao tema sejam excluídas caso ele seja excluído.
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) // Lado 1 da cardinalidade, mappedBy identificando onde está a chave estrangeira
	@JsonIgnoreProperties("postagens")
	private List<Postagem> postagens;
	
	public List<Postagem> getPostagens() {
		return postagens;
	}

	public void setPostagens(List<Postagem> postagens) {
		this.postagens = postagens;
	}

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