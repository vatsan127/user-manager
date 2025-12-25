package com.github.user_manager.api;

import com.github.user_manager.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Management", description = "APIs for managing users and their profiles")
public interface UserManagerApi {

    @Operation(summary = "Get all users", description = "Retrieves all users with their associated profiles")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all users",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Users.class),
                            examples = @ExampleObject(
                                    name = "User List Example",
                                    summary = "Sample response with user and profile",
                                    value = """
                                            [
                                                {
                                                    "id": 1,
                                                    "firstName": "srivatsan",
                                                    "lastName": "n",
                                                    "userProfiles": {
                                                        "id": 1,
                                                        "unit": "customer value",
                                                        "team": "marketing",
                                                        "phoneNumber": "9876543210",
                                                        "createdAt": "2025-12-24T12:05:44.817Z"
                                                    }
                                                }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping("/users")
    ResponseEntity<List<Users>> getUsers();

    @Operation(summary = "Create a new user", description = "Creates a new user with optional profile information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Users.class),
                            examples = @ExampleObject(
                                    name = "Created User Example",
                                    summary = "Sample response after creating a user",
                                    value = """
                                            {
                                                "id": 2,
                                                "firstName": "john",
                                                "lastName": "doe",
                                                "userProfiles": {
                                                    "id": 2,
                                                    "unit": "engineering",
                                                    "team": "backend",
                                                    "phoneNumber": "9876543211",
                                                    "createdAt": "2025-12-25T15:30:00.000Z"
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/users")
    ResponseEntity<Users> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User object with optional profile to be created",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Users.class),
                            examples = @ExampleObject(
                                    name = "Create User Request",
                                    summary = "Sample request to create a user with profile",
                                    value = """
                                            {
                                                "firstName": "john",
                                                "lastName": "doe",
                                                "userProfiles": {
                                                    "unit": "engineering",
                                                    "team": "backend",
                                                    "phoneNumber": "9876543211"
                                                }
                                            }
                                            """
                            )
                    )
            )
            @RequestBody Users user
    );

    @Operation(summary = "Update an existing user", description = "Updates user information and profile by user ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Users.class),
                            examples = @ExampleObject(
                                    name = "Updated User Example",
                                    summary = "Sample response after updating a user",
                                    value = """
                                            {
                                                "id": 1,
                                                "firstName": "srivatsan",
                                                "lastName": "narayanan",
                                                "userProfiles": {
                                                    "id": 1,
                                                    "unit": "customer value",
                                                    "team": "marketing",
                                                    "phoneNumber": "9876543210",
                                                    "createdAt": "2025-12-24T12:05:44.817Z"
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/users/{id}")
    ResponseEntity<Users> updateUser(
            @io.swagger.v3.oas.annotations.Parameter(
                    description = "ID of the user to update",
                    required = true,
                    example = "1"
            )
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated user object with profile information",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Users.class),
                            examples = @ExampleObject(
                                    name = "Update User Request",
                                    summary = "Sample request to update a user",
                                    value = """
                                            {
                                                "firstName": "srivatsan",
                                                "lastName": "narayanan",
                                                "userProfiles": {
                                                    "unit": "customer value",
                                                    "team": "marketing",
                                                    "phoneNumber": "9876543210"
                                                }
                                            }
                                            """
                            )
                    )
            )
            @RequestBody Users user
    );

    @Operation(summary = "Delete a user", description = "Deletes a user and their associated profile by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/users/{id}")
    ResponseEntity<Void> deleteUser(
            @io.swagger.v3.oas.annotations.Parameter(
                    description = "ID of the user to delete",
                    required = true,
                    example = "1"
            )
            @PathVariable Integer id
    );

}
