package com.agiletv.users.application.service;

import com.agiletv.users.application.dto.LocationCityTreeDto;
import com.agiletv.users.application.dto.LocationCountryTreeDto;
import com.agiletv.users.application.dto.LocationStateTreeDto;
import com.agiletv.users.application.dto.LocationTreeDto;
import com.agiletv.users.application.exception.InternalErrorException;
import com.agiletv.users.application.exception.NotFoundException;
import com.agiletv.users.application.exception.UserGeneratorServiceException;
import com.agiletv.users.application.mapper.UserMapper;
import com.agiletv.users.application.dto.LocationDto;
import com.agiletv.users.application.dto.UserDto;
import com.agiletv.users.application.service.dto.UserGeneratorDto;
import com.agiletv.users.application.service.dto.UserGeneratorLocationDto;
import com.agiletv.users.application.service.dto.UserGeneratorLoginDto;
import com.agiletv.users.application.service.dto.UserGeneratorNameDto;
import com.agiletv.users.application.service.dto.UserGeneratorPictureDto;
import com.agiletv.users.domain.model.IUser;
import com.agiletv.users.domain.model.PagedResult;
import com.agiletv.users.domain.model.User;
import com.agiletv.users.domain.repository.IUserRepository;
import com.agiletv.users.web.exception.BadRequestException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserGeneratorService userGeneratorService;
    private final IUserRepository repository;
    private final UserMapper mapper;

    @Autowired
    public UserService(UserGeneratorService userGeneratorService,
                       IUserRepository repository, UserMapper mapper) {
        this.userGeneratorService = userGeneratorService;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserDto create(UserDto userDto) throws BadRequestException {
        if (Boolean.TRUE.equals(repository.existByUsername(userDto.getUsername()))) {
            throw new BadRequestException(String.format("The user with %s already exist", userDto.getUsername()));
        }
        return mapper.toDto((User) repository.save(mapper.toDomain(userDto)));
    }

    @Override
    public UserDto getByUsername(String username) throws NotFoundException {
        return repository.findByUsername(username).map(user ->
                mapper.toDto((User) user)).orElseThrow(() -> new NotFoundException(String.format("The user with %s not exist", username)));
    }

    @Override
    public UserDto update(String username, UserDto userDto) throws NotFoundException {
        User user = repository.findByUsername(username).map(u -> (User) u).orElseThrow(
                () -> new NotFoundException(String.format("The user with %s not exist", username)));
        mapper.updateDomainFromDto(userDto, user);
        return mapper.toDto((User) repository.save(user));
    }

    @Override
    public void delete(String username) throws NotFoundException {
        if (Boolean.FALSE.equals(repository.existByUsername(username))) {
            throw new NotFoundException(String.format("The user with %s not exist", username));
        }
        repository.deleteByUsername(username);
    }

    @Override
    public PagedResult<UserDto> getAll(Integer pageNumber, Integer pageSize) throws BadRequestException {
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            List<UserDto> users = repository.findAll().stream()
                                            .map(user -> mapper.toDto((User) user))
                                            .collect(Collectors.toList());
            return PagedResult.<UserDto>builder()
                    .content(users)
                    .page(1)
                    .size(users.size())
                    .totalElements((long) users.size())
                    .totalPages(1)
                    .build();
        }
        if (pageNumber <= 0) {
            throw new BadRequestException("The page must not be less than 1");
        }
        PagedResult<IUser> userEntities = repository.findAll(pageNumber, pageSize);

        return PagedResult.<UserDto>builder()
                          .size(userEntities.getSize())
                          .page(userEntities.getPage())
                          .content(userEntities.getContent().stream()
                                               .map(user -> mapper.toDto((User)user))
                                               .collect(Collectors.toList()))
                          .totalPages(userEntities.getTotalPages())
                          .totalElements(userEntities.getTotalElements())
                          .build();
    }

    @Override
    public LocationTreeDto getTreeByLocation() {
        Map<String, LocationCountryTreeDto> countries = new HashMap<>();
        List<UserDto> users = repository.findAll().stream()
                                        .map(user -> mapper.toDto((User) user))
                                        .collect(Collectors.toList());

        users.forEach(user -> {
            String country = user.getLocation().getCountry();
            String state = user.getLocation().getState();
            String city = user.getLocation().getCity();

            countries.computeIfAbsent(country, k -> LocationCountryTreeDto.builder().states(new HashMap<>()).build());
            LocationCountryTreeDto countryDto = countries.get(country);

            countryDto.getStates().computeIfAbsent(state, k -> LocationStateTreeDto.builder().cities(new HashMap<>()).build());
            LocationStateTreeDto stateDto = countryDto.getStates().get(state);

            stateDto.getCities().computeIfAbsent(city, k -> LocationCityTreeDto.builder().users(new ArrayList<>()).build());
            LocationCityTreeDto cityDto = stateDto.getCities().get(city);

            cityDto.getUsers().add(user);
        });

        return LocationTreeDto.builder().countries(countries).build();
    }

    @Override
    public List<UserDto> generate(Integer quantityUsersToGenerate) throws BadRequestException, InternalErrorException {
        try {
            List<UserDto> userDtos = new ArrayList<>();

            for (UserDto userDto : this.getUsers(userGeneratorService.generateUsers(quantityUsersToGenerate))) {
                userDtos.add(this.create(userDto));
            }

            return userDtos;
        } catch (UserGeneratorServiceException exception) {
            throw new InternalErrorException(exception);
        }
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