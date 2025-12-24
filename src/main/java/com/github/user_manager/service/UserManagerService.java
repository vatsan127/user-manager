package com.github.user_manager.service;

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

}
