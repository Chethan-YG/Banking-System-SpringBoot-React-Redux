package com.bank.system.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bank.system.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
    public UserDetailsService userDetailsService()
    {
    	return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return userRepository.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("Username or email not found in database"));
						
			
			}
		};
    }
	
}
