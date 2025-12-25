package com.github.user_manager.repository;

import com.github.user_manager.entity.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfilesRepository extends JpaRepository<UserProfiles, Integer> {
}
