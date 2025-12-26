package com.github.user_manager.controller;

import com.github.user_manager.api.UserProfilesApi;
import com.github.user_manager.entity.UserProfiles;
import com.github.user_manager.service.UserProfilesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserProfilesController implements UserProfilesApi {

    private final UserProfilesService userProfilesService;

    public UserProfilesController(UserProfilesService userProfilesService) {
        this.userProfilesService = userProfilesService;
    }

    @Override
    public ResponseEntity<List<UserProfiles>> getProfiles() {
        List<UserProfiles> allProfiles = userProfilesService.getAllProfiles();
        return ResponseEntity.ok(allProfiles);
    }

    @Override
    public ResponseEntity<UserProfiles> getProfileById(Integer id) {
        UserProfiles profile = userProfilesService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }

    @Override
    public ResponseEntity<UserProfiles> createProfile(UserProfiles profile) {
        UserProfiles savedProfile = userProfilesService.saveProfile(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile);
    }

    @Override
    public ResponseEntity<UserProfiles> updateProfile(Integer id, UserProfiles profile) {
        UserProfiles updatedProfile = userProfilesService.updateProfile(id, profile);
        return ResponseEntity.ok(updatedProfile);
    }

    @Override
    public ResponseEntity<Void> deleteProfile(Integer id) {
        userProfilesService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

}
