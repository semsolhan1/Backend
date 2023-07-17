package com.handifarm.api.user.repository;

import com.handifarm.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {


    boolean existsByUserId(String userId);

}
