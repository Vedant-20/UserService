package com.hotel.user.service.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hotel.user.service.entities.Hotel;
import com.hotel.user.service.entities.Rating;
import com.hotel.user.service.entities.User;
import com.hotel.user.service.exceptions.ResourceNotFoundException;
import com.hotel.user.service.repositories.UserRepository;
import com.hotel.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public User saveUser(User user) {
		String randomuserId=UUID.randomUUID().toString();
		user.setUserId(randomuserId);
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		
		return userRepository.findAll();
	}

	@Override
	public User getUser(String userId) {
		
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with given id: " + userId));
		Rating[] ratingsOfUser=restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(), Rating[].class);
		logger.info(" {}",ratingsOfUser);
		List<Rating> ratings =Arrays.stream(ratingsOfUser).toList();
		List<Rating> ratingList=ratings.stream().map(rating ->{
			//api call to hotel service to get the hotel
			//set the hotel to rating
			//return the rating
			//http://localhost:8082/hotels/:hotelId
			ResponseEntity<Hotel> hotelDetails=restTemplate.getForEntity("http://HOTELSERVICE/hotels/"+rating.getHotelId(),Hotel.class);
			Hotel hotel=hotelDetails.getBody();		
			logger.info("response status code: {}",hotelDetails.getStatusCode());
			rating.setHotel(hotel);
			return rating;
			
		}).collect(Collectors.toList());
		user.setRatings(ratingList);
		return user;
	}

	@Override
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User updateUser(User user, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
