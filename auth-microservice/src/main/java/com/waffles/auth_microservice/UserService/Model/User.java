package com.waffles.auth_microservice.UserService.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waffles.auth_microservice.UserService.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @Column(nullable = false)
    private Role role;

    public User(String email, String password, Role role) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
