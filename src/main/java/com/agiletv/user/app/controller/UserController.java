package com.agiletv.user.app.controller;

import com.agiletv.user.app.exception.BadRequestException;
import com.agiletv.user.app.exception.InternalErrorException;
import com.agiletv.user.app.exception.NotFoundException;
import com.agiletv.user.context.infrastructure.service.RandomUserGenerateService;
import com.agiletv.user.context.infrastructure.service.UserService;
import com.agiletv.user.context.model.dto.PagedResultDto;
import com.agiletv.user.context.model.dto.UserDto;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RandomUserGenerateService randomUserGenerateService;

	@GetMapping
	PagedResultDto<UserDto> getAll(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false, defaultValue = "name") String sortBy,
			@RequestParam(required = false, defaultValue = "asc") String direction) throws BadRequestException {
		return userService.getAll(page, size, sortBy, direction);
	}

	@GetMapping("/{username}")
	public ResponseEntity<UserDto> getByUsername(@PathVariable String username) throws NotFoundException {
		return ResponseEntity.ok(userService.getByUsername(username));
	}

	@PostMapping
	public ResponseEntity<UserDto> create(@RequestBody UserDto userPojo) throws BadRequestException {
		return ResponseEntity.ok(userService.create(userPojo));
	}

	@PutMapping("/{username}")
	public ResponseEntity<UserDto> update(@PathVariable String username, @RequestBody UserDto userPojoToUpdate)
            throws NotFoundException {
		return ResponseEntity.ok(userService.update(username, userPojoToUpdate));
	}

	@DeleteMapping("/{username}")
	public ResponseEntity<Void> delete(@PathVariable String username) throws NotFoundException {
		userService.delete(username);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/generate/{number}")
	List<UserDto> generate(@PathVariable Integer number) throws InternalErrorException, BadRequestException {
		List<UserDto> users = randomUserGenerateService.generateUsers(number);

		for(UserDto user : users) {
			userService.create(user);
		}

		return users;
	}

	@GetMapping("/tree")
	public ResponseEntity<Map<String, Object>> getTree() {
		return ResponseEntity.ok(userService.getTree());
	}
}
