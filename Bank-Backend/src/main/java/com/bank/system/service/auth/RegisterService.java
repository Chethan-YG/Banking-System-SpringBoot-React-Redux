package com.bank.system.service.auth;

import com.bank.system.entity.User;

public interface RegisterService {
	public String registerUser(User user, String confirmPassword);
}
