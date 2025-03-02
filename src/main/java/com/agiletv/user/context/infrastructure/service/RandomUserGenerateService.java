package com.agiletv.user.context.infrastructure.service;

import com.agiletv.user.context.infrastructure.exception.UserGeneratorServiceException;
import com.agiletv.user.context.model.LocationDto;
import com.agiletv.user.context.model.UserGeneratorDto;
import com.agiletv.user.context.model.UserGeneratorLocationDto;
import com.agiletv.user.context.model.UserGeneratorLoginDto;
import com.agiletv.user.context.model.UserGeneratorNameDto;
import com.agiletv.user.context.model.UserGeneratorPictureDto;
import com.agiletv.user.context.model.UserDto;
import com.agiletv.user.context.service.UserGeneratorService;
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
public class RandomUserGenerateService implements UserGeneratorService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String apiUrl;

    public RandomUserGenerateService(@Value("${randomuser.api.url}") String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public List<UserDto> generateUsers(Integer quantityUsersToGenerate) throws UserGeneratorServiceException {
        List<UserGeneratorDto> userGeneratorDtos = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(apiUrl))
                                         .GET()
                                         .build();

        try {
            while(userGeneratorDtos.size() < quantityUsersToGenerate) {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonNode rootNode = MAPPER.readTree(response.body());
                    JsonNode resultsNode = rootNode.path("results");

                    List<UserGeneratorDto> users = MAPPER.readValue(resultsNode.toString(), new TypeReference<>() {});

                    for (UserGeneratorDto user : users) {
                        userGeneratorDtos.add(user);
                        if (userGeneratorDtos.size() == quantityUsersToGenerate) {
                            break;
                        }
                    }
                }
                else {
                    throw new UserGeneratorServiceException(response.body());
                }
            }
        } catch (Exception e) {
            throw new UserGeneratorServiceException(e);
        }

        return getUsers(userGeneratorDtos);
    }

    private List<UserDto> getUsers(List<UserGeneratorDto> userGeneratorDtos) {
        List<UserDto> users = new ArrayList<>();

        userGeneratorDtos.forEach(user -> users.add(UserDto.builder()
                                                           .email(user.getEmail())
                                                           .name(this.getName(user.getName()))
                                                           .gender(user.getGender())
                                                           .username(this.getUsername(user.getLogin()))
                                                           .location(this.getLocation(user.getLocation()))
                                                           .urlPhoto(this.getUrlPhoto(user.getPicture()))
                                                           .build()));

        return users;
    }

    private String getName(UserGeneratorNameDto name) {
        return Objects.isNull(name) ? null : String.format("%s %s", name.getFirst(), name.getLast());
    }

    private String getUsername(UserGeneratorLoginDto login) {
        return Objects.isNull(login) ? null : login.getUsername();
    }

    private LocationDto getLocation(UserGeneratorLocationDto location) {
        return Objects.isNull(location) ? null :
               LocationDto.builder()
                          .city(location.getCity())
                          .country(location.getCity())
                          .state(location.getState())
                          .build();
    }

    private String getUrlPhoto(UserGeneratorPictureDto picture) {
        return Objects.isNull(picture) ? null :
               !Strings.isEmpty(picture.getThumbnail()) ? picture.getThumbnail() :
               !Strings.isEmpty(picture.getLarge()) ? picture.getLarge() : picture.getMedium();
    }
}
