package com.agiletv.user.context.infrastructure.service;

import com.agiletv.user.app.exception.InternalErrorException;
import com.agiletv.user.context.infrastructure.exception.RandomUserGenerateServiceException;
import com.agiletv.user.context.model.dto.LocationDto;
import com.agiletv.user.context.model.dto.RandomUserGenerateDto;
import com.agiletv.user.context.model.dto.RandomUserGenerateLocationDto;
import com.agiletv.user.context.model.dto.RandomUserGenerateLoginDto;
import com.agiletv.user.context.model.dto.RandomUserGenerateNameDto;
import com.agiletv.user.context.model.dto.RandomUserGeneratePictureDto;
import com.agiletv.user.context.model.dto.UserDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RandomUserGenerateService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String apiUrl;

    public RandomUserGenerateService(@Value("${randomuser.api.url}") String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public List<UserDto> generateUsers(Integer quantityUsersToGenerate) throws InternalErrorException {
        List<RandomUserGenerateDto> randomUserGenerateDtos = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(apiUrl))
                                         .GET()
                                         .build();

        try {
            for (int i = 0; i < quantityUsersToGenerate; i++) {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonNode rootNode = MAPPER.readTree(response.body());
                    JsonNode resultsNode = rootNode.path("results");

                    randomUserGenerateDtos.addAll(MAPPER.readValue(
                            resultsNode.toString(),
                            new TypeReference<List<RandomUserGenerateDto>>() {}
                    ));
                }
                else {
                    throw new RandomUserGenerateServiceException(response.body());
                }
            }
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

        return getUsers(randomUserGenerateDtos);
    }

    private List<UserDto> getUsers(List<RandomUserGenerateDto> randomUserGenerateDtos) {
        List<UserDto> users = new ArrayList<>();

        randomUserGenerateDtos.forEach(user -> users.add(UserDto.builder()
                                                            .email(user.getEmail())
                                                            .name(this.getName(user.getName()))
                                                            .gender(user.getGender())
                                                            .username(this.getUsername(user.getLogin()))
                                                            .location(this.getLocation(user.getLocation()))
                                                            .urlPhoto(this.getUrlPhoto(user.getPicture()))
                                                                .build()));

        return users;
    }

    private String getName(RandomUserGenerateNameDto name) {
        return Objects.isNull(name) ? null : String.format("%s %s", name.getFirst(), name.getLast());
    }

    private String getUsername(RandomUserGenerateLoginDto login) {
        return Objects.isNull(login) ? null : login.getUsername();
    }

    private LocationDto getLocation(RandomUserGenerateLocationDto location) {
        return Objects.isNull(location) ? null :
               LocationDto.builder()
                          .city(location.getCity())
                          .country(location.getCity())
                          .state(location.getState())
                          .build();
    }

    private String getUrlPhoto(RandomUserGeneratePictureDto picture) {
        return Objects.isNull(picture) ? null :
               !Strings.isEmpty(picture.getThumbnail()) ? picture.getThumbnail() :
               !Strings.isEmpty(picture.getLarge()) ? picture.getLarge() : picture.getMedium();
    }
}
