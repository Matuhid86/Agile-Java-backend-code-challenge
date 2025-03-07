package com.agiletv.users.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location implements ILocation {
    private String city;
    private String state;
    private String country;
}
