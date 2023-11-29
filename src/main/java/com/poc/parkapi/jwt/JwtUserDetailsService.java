package com.poc.parkapi.jwt;

import com.poc.parkapi.entity.User;
import com.poc.parkapi.enums.Role;
import com.poc.parkapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        return new JwtUserDetails(user);
    }

    public JwtToken getTokenAuthorization(String username) {
        Role role = userService.findRoleByUsername(username);

        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }
}
