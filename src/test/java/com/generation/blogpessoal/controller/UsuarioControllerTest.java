package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import com.generation.blogpessoal.util.JwtHelper;
import com.generation.blogpessoal.util.TestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private static final String BASE_URL = "/usuarios";
	private static final String USUARIO_ADMIN = "root@root.com";
	private static final String SENHA_ADMIN = "rootroot";
	
	private String token;

	@BeforeAll
	void inicio() {
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Root", USUARIO_ADMIN, SENHA_ADMIN));
		token = JwtHelper.obterToken(testRestTemplate, USUARIO_ADMIN, SENHA_ADMIN);
	}

	@Test
	@DisplayName("01 - Deve Cadastrar um novo usuário com sucesso")
	void deveCadastrarUsuario() {
		// Given
		Usuario usuario = TestBuilder.criarUsuario(null, "Gabrielle Guimarães", "gabrielle@email.com.br", "gabi1234");

		// When

		// Corpo da Requisição
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);

		// Enviar a Requisição
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/cadastrar", HttpMethod.POST,
				requisicao, Usuario.class);

		// Then

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}

	@Test
	@DisplayName("02 - Não Deve Cadastrar usuário duplicado")
	void naoDeveCadastrarUsuarioDuplicado() {
		// Given
		Usuario usuario = TestBuilder.criarUsuario(null, "Luiza Guimarães", "luiza@email.com.br", "luiza1234");
		usuarioService.cadastrarUsuario(usuario);

		// When

		// Corpo da Requisição
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);

		// Enviar a Requisição
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/cadastrar", HttpMethod.POST,
				requisicao, Usuario.class);

		// Then

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		assertNull(resposta.getBody());
	}

	@Test
	@DisplayName("03 - Deve Atualizar os dados do usuário com sucesso")
	void deveAtualizarUsuario() {
		// Given

		// Cria o objeto da Classe Usuario
		Usuario usuario = TestBuilder.criarUsuario(null, "Daniel", "daniel@email.com.br", "daniel1234");

		// Cadastra o usuário e recupera o Id gerado
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);

		// Cria o objeto da Classe Usuario com os dados atualizados
		Usuario usuarioUpdate = TestBuilder.criarUsuario(usuarioCadastrado.get().getId(), "Daniel Araujo",
				"daniel_araujo@email.com.br", "abcd1234");

		// When

		// Cria a Requisição com Token (Cabeçalho com Token + Body com o objeto usuarioUpdate)
		HttpEntity<Usuario> requisicaoComToken = JwtHelper
				.criarRequisicaoComToken(usuarioUpdate, token);

		// Envia a Requisição
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange(BASE_URL + "/atualizar", 
				HttpMethod.PUT,	requisicaoComToken, Usuario.class);

		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("04 - Deve Listar todos os usuários")
	void deveListarTodosUsuarios() {
		
		// Given
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Kaue Dota", "kaue@email.com.br", "kaue1234"));
		usuarioService.cadastrarUsuario(
				TestBuilder.criarUsuario(null, "Edson Nascimento", "edson@email.com.br", "edson1234"));

		// When

		// Cria a Requisição com Token (Cabeçalho com Token + Body Vazio)
		HttpEntity<Void> requisicaoComToken = JwtHelper.criarRequisicaoComToken(token);

		// Envia a Requisição
		ResponseEntity<Usuario[]> resposta = testRestTemplate.exchange(BASE_URL + "/all", HttpMethod.GET,
				requisicaoComToken, Usuario[].class);

		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("05 - Deve buscar um usuário por ID")
	void deveBuscarUsuarioPorId() {
		// Given
		Optional<Usuario> usuario = usuarioService.cadastrarUsuario(
				TestBuilder.criarUsuario(null, "Fabiana Moraes", 
						"fabi_moraes@email.com.br", "12345678")
		);
		
		// When

		// Cria a Requisição com Token (Cabeçalho com Token + Body Vazio)
		HttpEntity<Void> requisicaoComToken = JwtHelper.criarRequisicaoComToken(token);

		// Envia a Requisição
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
		        BASE_URL + "/" + usuario.get().getId(), 
		        HttpMethod.GET, requisicaoComToken, Usuario.class);
		
		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertEquals("Fabiana Moraes", resposta.getBody().getNome());
	}

}