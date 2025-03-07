package com.agiletv.users.infrastructure.persistence.entity;

import com.agiletv.users.domain.model.IUser;
import com.agiletv.users.domain.model.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements IUser {
    @Id
    @Column(name = "username", unique = true, nullable = false, updatable = false)
    private String username;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    @Embedded
    @Column(name = "location")
    private LocationEntity location;
    @Column(name = "url_photo")
    private String urlPhoto;
}
