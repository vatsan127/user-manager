package com.github.user_manager.api;

import com.github.user_manager.entity.UserProfiles;
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

@Tag(name = "Profile Management", description = "APIs for managing user profiles")
public interface UserProfilesApi {

    @Operation(summary = "Get all profiles", description = "Retrieves all user profiles")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all profiles",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserProfiles.class),
                            examples = @ExampleObject(
                                    name = "Profile List Example",
                                    summary = "Sample response with profiles",
                                    value = """
                                            [
                                                {
                                                    "id": 1,
                                                    "unit": "customer value",
                                                    "team": "marketing",
                                                    "phoneNumber": "9876543210",
                                                    "createdAt": "2025-12-24T12:05:44.817Z"
                                                }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping("/profiles")
    ResponseEntity<List<UserProfiles>> getProfiles();

    @Operation(summary = "Get profile by ID", description = "Retrieves a user profile by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the profile",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserProfiles.class),
                            examples = @ExampleObject(
                                    name = "Profile Example",
                                    summary = "Sample profile response",
                                    value = """
                                            {
                                                "id": 1,
                                                "unit": "customer value",
                                                "team": "marketing",
                                                "phoneNumber": "9876543210",
                                                "createdAt": "2025-12-24T12:05:44.817Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/profiles/{id}")
    ResponseEntity<UserProfiles> getProfileById(
            @io.swagger.v3.oas.annotations.Parameter(
                    description = "ID of the profile to retrieve",
                    required = true,
                    example = "1"
            )
            @PathVariable Integer id
    );

    @Operation(summary = "Create a new profile", description = "Creates a new standalone user profile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Profile successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserProfiles.class),
                            examples = @ExampleObject(
                                    name = "Created Profile Example",
                                    summary = "Sample response after creating a profile",
                                    value = """
                                            {
                                                "id": 2,
                                                "unit": "engineering",
                                                "team": "backend",
                                                "phoneNumber": "9876543211",
                                                "createdAt": "2025-12-25T15:30:00.000Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/profiles")
    ResponseEntity<UserProfiles> createProfile(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Profile object to be created",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserProfiles.class),
                            examples = @ExampleObject(
                                    name = "Create Profile Request",
                                    summary = "Sample request to create a profile",
                                    value = """
                                            {
                                                "unit": "engineering",
                                                "team": "backend",
                                                "phoneNumber": "9876543211"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody UserProfiles profile
    );

    @Operation(summary = "Update an existing profile", description = "Updates profile information by profile ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserProfiles.class),
                            examples = @ExampleObject(
                                    name = "Updated Profile Example",
                                    summary = "Sample response after updating a profile",
                                    value = """
                                            {
                                                "id": 1,
                                                "unit": "sales",
                                                "team": "enterprise",
                                                "phoneNumber": "9876543210",
                                                "createdAt": "2025-12-24T12:05:44.817Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/profiles/{id}")
    ResponseEntity<UserProfiles> updateProfile(
            @io.swagger.v3.oas.annotations.Parameter(
                    description = "ID of the profile to update",
                    required = true,
                    example = "1"
            )
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated profile object",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserProfiles.class),
                            examples = @ExampleObject(
                                    name = "Update Profile Request",
                                    summary = "Sample request to update a profile",
                                    value = """
                                            {
                                                "unit": "sales",
                                                "team": "enterprise",
                                                "phoneNumber": "9876543210"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody UserProfiles profile
    );

    @Operation(summary = "Delete a profile", description = "Deletes a user profile by profile ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @DeleteMapping("/profiles/{id}")
    ResponseEntity<Void> deleteProfile(
            @io.swagger.v3.oas.annotations.Parameter(
                    description = "ID of the profile to delete",
                    required = true,
                    example = "1"
            )
            @PathVariable Integer id
    );

}
