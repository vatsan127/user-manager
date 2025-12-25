package com.github.user_manager.controller;

import com.github.user_manager.api.UserManagerApi;
import com.github.user_manager.entity.Users;
import com.github.user_manager.service.UserManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserManagerController implements UserManagerApi {

    private final UserManagerService userManagerService;

    public UserManagerController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @Override
    public ResponseEntity<List<Users>> getUsers() {
        List<Users> allUsers = userManagerService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @Override
    public ResponseEntity<Users> createUser(Users user) {
        Users savedUser = userManagerService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Override
    public ResponseEntity<Users> updateUser(Integer id, Users user) {
        Users updatedUser = userManagerService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Integer id) {
        userManagerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
