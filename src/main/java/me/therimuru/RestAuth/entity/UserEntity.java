package me.therimuru.RestAuth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, insertable = false)
    private Long id;

    @Column(nullable = false, length = 16)
    private String name;

    @Column(nullable = false, length = 32)
    private String surname;

    @Column(nullable = false, length = 16, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;
}