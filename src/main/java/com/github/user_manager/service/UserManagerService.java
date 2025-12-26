package com.github.user_manager.service;

import com.github.user_manager.entity.Users;
import com.github.user_manager.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManagerService {

    private final UsersRepository usersRepository;

    public UserManagerService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users saveUser(Users user) {
        return usersRepository.save(user);
    }

    public Users updateUser(Integer id, Users user) {
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());

        if (user.getUserProfiles() != null) {
            if (existingUser.getUserProfiles() != null) {
                existingUser.getUserProfiles().setUnit(user.getUserProfiles().getUnit());
                existingUser.getUserProfiles().setTeam(user.getUserProfiles().getTeam());
                existingUser.getUserProfiles().setPhoneNumber(user.getUserProfiles().getPhoneNumber());
            } else {
                existingUser.setUserProfiles(user.getUserProfiles());
            }
        }

        return usersRepository.save(existingUser);
    }

    public void deleteUser(Integer id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        usersRepository.delete(user);
    }

}
