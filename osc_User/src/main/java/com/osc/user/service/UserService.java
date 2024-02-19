package com.osc.user.service;

import java.util.Map;

import com.grpc.user.UserResponse;
import com.osc.user.payloads.UserDto;
import com.osc.user.payloads.ValidateOtpDto;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.checkerframework.checker.units.qual.K;
import org.springframework.http.ResponseEntity;

public interface UserService {

	UserDto addUser(UserDto user);
	
	boolean isEmailExists(String mail);
	
	boolean sendMail(Map<String,String> mailMap);
	
	void storeInCache(Map<String,String> mailMap);
	
	UserDto getUserDetails(String userId);

	void otpStoreInStream(Map<String, String> mailMap);


	ResponseEntity<?> validateOtp(ValidateOtpDto validateOtpDto);
}
