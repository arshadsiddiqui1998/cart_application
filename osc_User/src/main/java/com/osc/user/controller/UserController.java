package com.osc.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.osc.user.payloads.ValidateOtpDto;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osc.user.encryption.EncryptDecrypt;
import com.osc.user.payloads.UserDto;
import com.osc.user.security.JwtHelper;
import com.osc.user.service.UserService;

import response.ApiResponse;

@RestController
@RequestMapping("/user")
public class UserController {

	private final Logger logger =  LoggerFactory.getLogger(UserController.class);


	Map<String,String> map = new HashMap<>();

	@Autowired
	UserService userService;


	@Autowired
	private JwtHelper helper;

	int count;
	
	Map<String, Integer> invalidOtpCount = new HashMap<String, Integer>();

	ApiResponse apiResp = null;

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody UserDto dto) {
		apiResp = new ApiResponse();
		try {
			if(userService.isEmailExists(dto.getEmail())) {
				apiResp.setStatus(false);
				apiResp.setMessage("Email with this mail already present");
				apiResp.setCode(30);
				apiResp.setDataObject(null);

				return ResponseEntity.status(200).body(apiResp);
			}

			//Otp Generation
			int otp = 100000 + new Random().nextInt(900000);

			//Random UserId
			String userId = "user"+new Random().nextInt(999);
			dto.setUserId(userId);

			//jwt token creation
			String token = this.helper.generateToken(dto, otp); 

			Map<String, String> mailMap = new HashMap<String, String>();
			Map<String, String> otpValidate = new HashMap<String, String>();

			mailMap.put("To",dto.getEmail());
			mailMap.put("subject", "OTP for validation");
			mailMap.put("otp", "**Please enter this OTP  to complete the verification process. : "+otp+"**\n\n");
			mailMap.put("userId", "Dear User, your UserID is: " + userId + "\n\n");
			//call to mail service for required functionality
			userService.sendMail(mailMap);

			mailMap.put("contact", dto.getContact());
			mailMap.put("name", dto.getName());
			mailMap.put("userId", dto.getUserId());
			mailMap.put("isClear", "false");
			mailMap.put("dob", dto.getDob()!=null ? dto.getDob().toString():"");

			//call to cache service to store data in cache temporary
			userService.storeInCache(mailMap);

			otpValidate.put("userId",dto.getUserId());
			otpValidate.put("otp",String.valueOf(otp));

			userService.otpStoreInStream(otpValidate);



			//dataObject creating
			map.put("userId", userId);
			apiResp.setStatus(true);
			apiResp.setToken(token);
			apiResp.setCode(200);
			apiResp.setMessage("successful");
			apiResp.setDataObject(map);
		}
		catch (Exception e) {
			logger.info("message : "+e.getMessage()+" Cause : "+e.getCause()+" Stack Trace :"+e.getStackTrace());
			apiResp.setStatus(true);
			apiResp.setMessage("unexpected error happens");
			apiResp.setCode(0);
			apiResp.setDataObject(null);
			
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}

		//Sending Response to UI
		return ResponseEntity.status(200).body(apiResp);

	}

	@PostMapping("/validate-otp")
	public ResponseEntity<?> validateOtp(@RequestBody ValidateOtpDto validateOtp) {
		System.out.println(validateOtp);
		ResponseEntity<?> responseEntity = userService.validateOtp(validateOtp);
		return null;
	}



	@PostMapping("/validateotp")
	public ResponseEntity<?> validateOtp(@RequestBody Map<String,Object> map,@RequestHeader("Authorization")String token ){
		apiResp = new ApiResponse();
		try {
			token = token !=null?token.substring(7) : "IvnXGDN40MKJJSpQl9fUJg==";
			Boolean validateToken = this.helper.validateToken(token, map);
			

			String userId = (String) this.helper.getClaimFromToken(token, (x->x.get("userId")));

			if(!userId.equals(map.get("userId"))) {
				apiResp.setStatus(false);
				apiResp.setMessage("User Id Not Valid");
				apiResp.setCode(1999);

				return ResponseEntity.status(200).body(apiResp);
			}

			if (validateToken) {
				apiResp.setStatus(true);
				apiResp.setMessage("valid Otp");
				apiResp.setCode(500);

				return ResponseEntity.status(200).body(apiResp);
			}else {
				count = invalidOtpCount.get(map.get("userId"));
				count++;
				invalidOtpCount.put(map.get("userId").toString(), count);
			}

			if(count == 3) {
				invalidOtpCount.put(map.get("userId").toString(), 0);
				apiResp.setStatus(false);
				apiResp.setMessage("Invalid Otp 3 Attempts Completed");
				apiResp.setCode(301);

				return ResponseEntity.status(200).body(apiResp);
			}



			apiResp.setStatus(false);
			apiResp.setMessage("Invalid Otp");
			apiResp.setCode(502);

			return ResponseEntity.status(200).body(apiResp);

		}
		catch (Exception e) {
			logger.info("message : "+e.getMessage()+" Cause : "+e.getCause()+" Stack Trace :"+e.getStackTrace());
			apiResp.setStatus(false);
			apiResp.setMessage("Unexpected Error Happened");
			apiResp.setCode(0);

			return ResponseEntity.status(200).body(apiResp);
		}

	}

	@PostMapping("/addUserDetails")
	public ResponseEntity<?> createUser(@RequestBody UserDto user){
		apiResp = new ApiResponse();
		try {
			UserDto userDetails = userService.getUserDetails(user.getUserId());

			String encryptedPassword = new EncryptDecrypt().encrypt(user.getPassword());
			userDetails.setPassword(encryptedPassword);

			userService.addUser(userDetails);

			map.put("userId", user.getUserId());
			map.put("isClear", "true");
			//Clear User Data from cache
			userService.storeInCache(map);

			apiResp.setStatus(true);
			apiResp.setMessage("User Created Successfully");
			apiResp.setCode(200);
			
			return ResponseEntity.status(200).body(apiResp);

		}catch (Exception e) {
			e.printStackTrace();
			apiResp.setStatus(false);
			apiResp.setMessage("Unexpected Error Happened");
			apiResp.setCode(0);

			return ResponseEntity.status(200).body(apiResp);
		}

	}

}
