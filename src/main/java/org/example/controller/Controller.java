package org.example.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@OpenAPIDefinition(info = @Info(title = "Demo Swagger API", version = "v1", description = "Documentation of My API",
        contact = @Contact(name = "Epam Support", email = "support@epam.com"),
        license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")))
@Tag(name = "User", description = "Operations related to user entities")
@Slf4j
@RestController
@RequestMapping("/api")
public class Controller {
    private static final Set<User> users = new HashSet<>();

    @Operation(summary = "Create a user by name", description = "Returns a single user")
    @ApiResponse(responseCode = "201", description = "User created successfully",
            content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))},
            headers = {
                    @Header(name = "X-Rate-Limit", description = "calls per hour allowed by the user")})
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @Parameters({
            @Parameter(name = "name", description = "User Name", required = true)
    })
    @PostMapping("/create/{name}")
    public ResponseEntity<User> createUser(@PathVariable("name") String name) {
        log.info("Attempting to create a new user with name: {}", name);
        Optional<User> opUser = getUserFromUsers(name);
        User user = new User();
        if (!opUser.isPresent()) {
            user.setId(Math.abs(new SecureRandom().nextLong()));
            user.setName(name);
            user.setCreatedAt(getDateFromLocalDate());

            users.add(user);
        }

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get a user by ID", description = "Returns a single user")
    @ApiResponse(responseCode = "200", description = "User got successfully",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))},
            headers = {
                    @Header(name = "X-Rate-Limit", description = "calls per hour allowed by the user")})
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @GetMapping("/read/{name}")
    public ResponseEntity<User> readUser(@PathVariable("name") String name) {
        log.info("Attempting to get a user with name: {}", name);

        User user = users.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()

                .orElse(new User());
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update a user by name", description = "Returns a list of users")
    @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))},
            headers = {
                    @Header(name = "X-Rate-Limit", description = "calls per hour allowed by the user")})
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PutMapping("/update/{oldName}/{newName}")
    public ResponseEntity<Set<User>> updateUser(@PathVariable("oldName") String oldName, @PathVariable("newName") String newName) {
        log.info("Attempting to update a user with name: {}", newName);
        users.stream()
                .peek(u -> {
                    if (u.getName().equals(oldName)) {
                        u.setName(newName);
                        u.setCreatedAt(getDateFromLocalDate());
                    }
                })
                .collect(Collectors.toSet());

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Delete a user by name", description = "Returns nothing")
    @ApiResponse(responseCode = "200", description = "User deleted successfully",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))},
            headers = {
                    @Header(name = "X-Rate-Limit", description = "calls per hour allowed by the user")})
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteUser(@PathVariable("name") String name) {
        log.info("Attempting to delete a user with name: {}", name);
        Optional<User> opUser = getUserFromUsers(name);
        if (opUser.isPresent()) {
            users.remove(opUser.get());
        }

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Partial update a user by name", description = "Returns a list of users")
    @ApiResponse(responseCode = "200", description = "User partial updated successfully",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))},
            headers = {
                    @Header(name = "X-Rate-Limit", description = "calls per hour allowed by the user")})
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PatchMapping("/patch/{oldName}/{newName}")
    public ResponseEntity<Set<User>> patchUser(@PathVariable("oldName") String oldName, @PathVariable("newName") String newName) {
        log.info("Attempting to patch a user with name: {}", newName);
        users.stream()
                .peek(u -> {
                    if (u.getName().equals(oldName)) {
                        u.setName(newName);
                    }
                })
                .collect(Collectors.toSet());

        return ResponseEntity.ok(users);
    }

    private Optional<User> getUserFromUsers(String name) {
        return users.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst();
    }

    private Date getDateFromLocalDate() {
        return Date.from(LocalDate.now()
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}