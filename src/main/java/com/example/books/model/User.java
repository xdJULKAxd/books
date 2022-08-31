package com.example.books.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private Long id;

    @Column(name="name", nullable = false, unique = true)
    private String username;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="role")
    private String role;

    @Column(name="password", nullable = false)
    private String password;

    @JsonIgnore
    public String getPassword(){
        return password;
    }

    @JsonProperty
    public void setPassword(String password){
        this.password = password;
    }

}