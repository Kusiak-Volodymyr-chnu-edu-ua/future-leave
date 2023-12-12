package com.voltor.futureleave;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.service.UserService;

@Profile ("h2")
@Component
public class DemoData {

	public DemoData(UserService userService) {
		User user = new User();
		user.setLogin("demo");
		user.setPassword("demo");
		user.setFirstName("demo");
		user.setLastName("demo");
		user.setUserRole(Role.SESSION_USER);
		userService.create(user);
	}
	
}
