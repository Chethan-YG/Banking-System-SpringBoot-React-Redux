package com.bank.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
public class User implements UserDetails{
	
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;    
    
    @NotEmpty(message = "The First name field cannot be empty.")
    @Size(min=3, message = "The first nmae field must greater that 3 characters")
    @Column(name= "firstName")
    @JsonProperty("firstname")
    private String firstname;
    
    @NotEmpty(message = "The Last name field cannot be empty.")
    @Column(name= "lastName")
    @JsonProperty("lastname")
    private String  lastname;
    
    @Email
    @NotEmpty(message = "Email field cannot be empty.")
    @Pattern(regexp = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})", message="Please enter a valid email.")
    private String  email;

    @NotEmpty(message = "Password field cannot be empty.")
    @NotNull
    private String  password;
    private String  token;
    private String  code;
    private int verified;
    private LocalDate verifiedAt;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
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
	public boolean isEnabled()
	{
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
}
