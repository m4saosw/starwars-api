package br.com.massao.api.starwars.security.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity(name = "Profile")
@Table(name="profile")
public class ProfileModel implements GrantedAuthority {
	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getAuthority() {
		return name;
	}
	
}
