package com.agiletv.users.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements IUser {
    private String username;
    private String name;
    private String email;
    private Gender gender;
    private Location location;
    private String urlPhoto;
}
