package com.osc.user.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

import com.osc.user.payloads.UserDto;
import com.osc.user.security.JwtHelper;
import com.osc.user.service.LoginService;
import com.osc.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import response.ApiResponse;

@RestController
@RequestMapping("/user")
public class LoginController {

	@Autowired
	LoginService service;

	ApiResponse apiResp = null;

	private final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody UserDto userDto,HttpServletRequest request){
		apiResp = new ApiResponse();
		try {

			if(!service.isUserIdValid(userDto.getUserId())) {
				apiResp.setCode(201);
				apiResp.setMessage("User not found for this userId");
				apiResp.setStatus(false);

				return ResponseEntity.status(HttpStatus.OK).body(apiResp);
			}

			if(!service.isPasswordValid(userDto.getPassword())) {
				apiResp.setCode(202);
				apiResp.setMessage("User not found for this password");
				apiResp.setStatus(false);

				return ResponseEntity.status(HttpStatus.OK).body(apiResp);
			}

			//Check if user is already logedIn
			if(service.isUserSessionExists(userDto.getUserId(),userDto.getLoginDevice())) {
				apiResp.setCode(204);
				apiResp.setMessage("Session Already Exists");
				apiResp.setStatus(false);

				return ResponseEntity.status(HttpStatus.OK).body(apiResp);
			}else {
				Map<String, String> sessionMap = new HashMap<String, String>();

				sessionMap.put("sessionId", request.getSession().getId()+"_"+userDto.getLoginDevice());
				sessionMap.put("userId", userDto.getUserId()+"_"+userDto.getLoginDevice());
				sessionMap.put("device", userDto.getLoginDevice());
				sessionMap.put("login", "true");
				sessionMap.put("loginTime",dateToString(new Date()));
				
				service.saveUserSession(sessionMap);

				service.produceInSession(sessionMap);

				sessionMap =  new HashMap<>();
				sessionMap.put("sessionId", request.getSession().getId()+"_"+userDto.getLoginDevice());
				sessionMap.put("name", service.getUserName(userDto.getUserId()));

				apiResp.setCode(200);
				apiResp.setDataObject(sessionMap);
				apiResp.setMessage("successful login");
				apiResp.setStatus(true);

				return ResponseEntity.status(HttpStatus.OK).body(apiResp);

			}

		}catch (Exception e) {
			logger.info("MESSAGE : "+e.getMessage()+" CAUSE : "+e.getCause()+" STACK TRACE : "+e.getStackTrace());
			apiResp.setCode(0);
			apiResp.setMessage("Unexpected Error 505");
			apiResp.setStatus(false);

			return ResponseEntity.status(HttpStatus.OK).body(apiResp);
		}

	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestBody UserDto userDto){
		apiResp = new ApiResponse();
		UserDto dto = service.findByEmailId(userDto.getEmail());

		if(dto != null) {

			//Otp Generation
			int otp = new Random().nextInt(999999);

			//jwt token creation
			String token = new JwtHelper().generateToken(dto, otp); 

			Map<String, String> mailMap = new HashMap<String, String>();
			mailMap.put("To",userDto.getEmail());
			mailMap.put("subject", "Forgot Password OTP");
			mailMap.put("body", otp+"");
			//call to mail service for required functionality
			userService.sendMail(mailMap);

			apiResp.setCode(200);
			apiResp.setToken(token);
			apiResp.setMessage("valid email id");
			apiResp.setStatus(true);

			return ResponseEntity.status(HttpStatus.OK).body(apiResp);
		}else {
			apiResp.setCode(199);
			apiResp.setMessage("invalid email id");
			apiResp.setStatus(false);

			return ResponseEntity.status(HttpStatus.OK).body(apiResp);
		}

	}

	@PostMapping("/validateOTPForForgotPassword")
	public ResponseEntity<?> validateOtp(@RequestBody Map<String, Object> reqBody,@RequestHeader("Authorization")String token){
		apiResp = new ApiResponse();
		JwtHelper helper = new JwtHelper();
		token = token !=null?token.substring(7) : "";

		final Integer otp = (Integer) helper.getClaimFromToken(token, (x->x.get("otp")));
		final String emailId = (String) helper.getClaimFromToken(token, (x->x.get("mail")));

		if(emailId.equals(reqBody.get("email").toString()) && otp.toString().equals(reqBody.get("OTP").toString())){
			apiResp.setCode(200);
			apiResp.setMessage("valid email id");
			apiResp.setStatus(true);

			return ResponseEntity.status(HttpStatus.OK).body(apiResp);
		}

		apiResp.setCode(199);
		apiResp.setMessage("Invalid 	OTP");
		apiResp.setStatus(false);

		return ResponseEntity.status(HttpStatus.OK).body(apiResp);

	}
	
	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody UserDto userDto){
		apiResp = new ApiResponse();
		boolean isUpdated = service.isPasswordChanged(userDto.getEmail(), userDto.getPassword());
		
		if(!isUpdated) {
			
			apiResp.setCode(199);
			apiResp.setMessage("Something went wrong");
			apiResp.setStatus(false);

			return ResponseEntity.status(HttpStatus.OK).body(apiResp);
			
		}
		
		apiResp.setCode(200);
		apiResp.setMessage("password changed successfully");
		apiResp.setStatus(true);

		return ResponseEntity.status(HttpStatus.OK).body(apiResp);

	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(@RequestBody UserDto userDto,HttpServletRequest request){
		apiResp = new ApiResponse();
		try {
		String device =  userDto.getSessionId() != null ? userDto.getSessionId().split("_")[1] : "web";
		
		Map<String, String> sessionMap = new HashMap<String, String>();

		sessionMap.put("sessionId", userDto.getSessionId());
		sessionMap.put("userId", userDto.getUserId()+"_"+device);
		sessionMap.put("device", device);
		sessionMap.put("login", "false");
		sessionMap.put("logoutTime",dateToString(new Date()));

		service.produceInSession(sessionMap);

		request.getSession().invalidate();
		
		}catch (Exception e) {
			logger.info("MESSAGE : "+e.getMessage()+" CAUSE : "+e.getCause()+" STACK TRACE : "+e.getStackTrace());
			apiResp.setCode(0);
			apiResp.setMessage("Something went wrong");
			apiResp.setStatus(false);

			return ResponseEntity.status(HttpStatus.OK).body(apiResp);

		}

		apiResp.setCode(200);
		apiResp.setMessage("User Successfully Logged Out");
		apiResp.setStatus(true);

		return ResponseEntity.status(HttpStatus.OK).body(apiResp);
		
	}
	
	private String dateToString(Date date) {

		String pattern = "MM/dd/yyyy HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		Date today = Calendar.getInstance().getTime();

		return df.format(today);
	}

}
