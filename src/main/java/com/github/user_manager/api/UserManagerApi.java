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
import org.springframework.web.bind.annotation.GetMapping;

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

}
