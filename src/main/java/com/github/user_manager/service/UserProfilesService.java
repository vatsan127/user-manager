package com.github.user_manager.service;

import com.github.user_manager.entity.UserProfiles;
import com.github.user_manager.repository.UserProfilesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfilesService {

    private final UserProfilesRepository userProfilesRepository;

    public UserProfilesService(UserProfilesRepository userProfilesRepository) {
        this.userProfilesRepository = userProfilesRepository;
    }

    public List<UserProfiles> getAllProfiles() {
        return userProfilesRepository.findAll();
    }

    public UserProfiles getProfileById(Integer id) {
        return userProfilesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
    }

    public UserProfiles saveProfile(UserProfiles profile) {
        return userProfilesRepository.save(profile);
    }

    public UserProfiles updateProfile(Integer id, UserProfiles profile) {
        UserProfiles existingProfile = userProfilesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));

        existingProfile.setUnit(profile.getUnit());
        existingProfile.setTeam(profile.getTeam());
        existingProfile.setPhoneNumber(profile.getPhoneNumber());

        return userProfilesRepository.save(existingProfile);
    }

    public void deleteProfile(Integer id) {
        UserProfiles profile = userProfilesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
        userProfilesRepository.delete(profile);
    }

}
