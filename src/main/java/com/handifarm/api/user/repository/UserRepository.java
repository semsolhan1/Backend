package com.handifarm.api.user.repository;

import com.handifarm.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserId(String userId);

    User findByUserNick(String userNick);

}
