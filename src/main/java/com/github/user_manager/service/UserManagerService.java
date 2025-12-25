package com.github.user_manager.service;

import com.github.user_manager.entity.UserProfiles;
import com.github.user_manager.entity.Users;
import com.github.user_manager.repository.UserProfilesRepository;
import com.github.user_manager.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManagerService {

    private final UsersRepository usersRepository;
    private final UserProfilesRepository userProfilesRepository;

    public UserManagerService(UserProfilesRepository userProfilesRepository, UsersRepository usersRepository) {
        this.userProfilesRepository = userProfilesRepository;
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users saveUser(Users user) {
        // If user has a profile, save it first (since there's no cascade configured)
        if (user.getUserProfiles() != null) {
            UserProfiles profile = user.getUserProfiles();
            UserProfiles savedProfile = userProfilesRepository.save(profile);
            user.setUserProfiles(savedProfile);
        }

        // Save and return the user
        return usersRepository.save(user);
    }

    public Users updateUser(Integer id, Users user) {
        // Check if user exists
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update user fields
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());

        // Update profile if provided
        if (user.getUserProfiles() != null) {
            if (existingUser.getUserProfiles() != null) {
                // Update existing profile
                UserProfiles existingProfile = existingUser.getUserProfiles();
                existingProfile.setUnit(user.getUserProfiles().getUnit());
                existingProfile.setTeam(user.getUserProfiles().getTeam());
                existingProfile.setPhoneNumber(user.getUserProfiles().getPhoneNumber());
                userProfilesRepository.save(existingProfile);
            } else {
                // Create new profile
                UserProfiles newProfile = userProfilesRepository.save(user.getUserProfiles());
                existingUser.setUserProfiles(newProfile);
            }
        }

        // Save and return updated user
        return usersRepository.save(existingUser);
    }

    public void deleteUser(Integer id) {
        // Check if user exists
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Delete user (profile will remain unless explicitly deleted)
        usersRepository.delete(user);

        // Optionally delete the associated profile if it exists
        if (user.getUserProfiles() != null) {
            userProfilesRepository.delete(user.getUserProfiles());
        }
    }

}
