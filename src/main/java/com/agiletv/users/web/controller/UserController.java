package com.agiletv.users.web.controller;

import com.agiletv.users.application.dto.LocationTreeDto;
import com.agiletv.users.application.exception.InternalErrorException;
import com.agiletv.users.application.exception.NotFoundException;
import com.agiletv.users.application.service.IUserService;
import com.agiletv.users.application.dto.UserDto;
import com.agiletv.users.domain.model.PagedResult;
import com.agiletv.users.web.exception.BadRequestException;
import java.util.List;
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
	private IUserService userService;

	@GetMapping
	PagedResult<UserDto> getAll(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) throws BadRequestException {
		return userService.getAll(page, size);
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
		return userService.generate(number);
	}

	@GetMapping("/tree")
	public ResponseEntity<LocationTreeDto> getTree() {
		return ResponseEntity.ok(userService.getTreeByLocation());
	}
}
