package com.agiletv.users.application.service;

import com.agiletv.users.application.exception.UserGeneratorServiceException;
import com.agiletv.users.application.service.dto.UserGeneratorDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RandomUserGenerateService implements UserGeneratorService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String apiUrl;

    public RandomUserGenerateService(@Value("${randomuser.api.url}") String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public List<UserGeneratorDto> generateUsers(Integer quantityUsersToGenerate) throws UserGeneratorServiceException {
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

        return userGeneratorDtos;
    }
}
