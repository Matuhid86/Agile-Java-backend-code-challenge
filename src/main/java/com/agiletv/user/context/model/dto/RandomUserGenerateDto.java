package com.agiletv.user.context.model.dto;

import com.agiletv.user.context.model.Gender;
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
public class RandomUserGenerateDto {
    @JsonProperty("name")
    private RandomUserGenerateNameDto name;
    @JsonProperty("location")
    private RandomUserGenerateLocationDto location;
    @JsonProperty("email")
    private String email;
    @JsonProperty("gender")
    private Gender gender;
    @JsonProperty("picture")
    private RandomUserGeneratePictureDto picture;
    @JsonProperty("login")
    private RandomUserGenerateLoginDto login;
}