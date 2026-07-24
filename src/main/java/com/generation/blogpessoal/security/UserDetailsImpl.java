package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

/*
essa classe não possui setters, porque seus objetos armazenarão 
os dados recebidos do usuário, sem necessidade de mudança.
 */
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;

	public UserDetailsImpl(Usuario user) {
		this.username = user.getUsuario();
		this.password = user.getSenha();
	}
	
	//ainda não foram implementados roles, por isso retorna lista vazia
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();  
	}
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public String getUsername() {
		return username;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
}
