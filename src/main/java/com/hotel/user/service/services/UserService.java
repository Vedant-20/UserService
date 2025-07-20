package com.hotel.user.service.services;



import java.util.List;

import com.hotel.user.service.entities.User;

public interface UserService {

	//create user
	User saveUser(User user);
	
	//get all users
	List<User> getAllUser();
	
	//get single user
	User getUser(String userId);
	
	//delete user
	void deleteUser(String userId);
	
	//update user
	User updateUser(User user, String userId);
}
