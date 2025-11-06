package me.therimuru.RestAuth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, insertable = false)
    private Long id;

    @Column(nullable = false, length = 16)
    private String name;

    @Column(nullable = false, length = 32)
    private String surname;

    @Column(nullable = false, length = 16, unique = true)
    private String username;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    @JsonIgnore
    private String password;
}