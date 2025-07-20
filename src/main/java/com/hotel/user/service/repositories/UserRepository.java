package com.hotel.user.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.user.service.entities.User;

public interface UserRepository extends JpaRepository<User, String> {

}
