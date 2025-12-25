package com.github.user_manager.controller;

import com.github.user_manager.api.UserManagerApi;
import com.github.user_manager.entity.Users;
import com.github.user_manager.service.UserManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserManagerController implements UserManagerApi {

    private final UserManagerService userManagerService;

    public UserManagerController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @Override
    public ResponseEntity<List<Users>> getUsers() {
        List<Users> allUsers = userManagerService.getAllUsers();
        log.info("UserManagerController :: getUsers :: {}", allUsers);
        return ResponseEntity.ok(allUsers);
    }

}
