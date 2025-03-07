package com.agiletv.users.application.service.dto;

import com.agiletv.users.domain.model.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGeneratorDto {
    @JsonProperty("name")
    private UserGeneratorNameDto name;
    @JsonProperty("location")
    private UserGeneratorLocationDto location;
    @JsonProperty("email")
    private String email;
    @JsonProperty("gender")
    private Gender gender;
    @JsonProperty("picture")
    private UserGeneratorPictureDto picture;
    @JsonProperty("login")
    private UserGeneratorLoginDto login;
}