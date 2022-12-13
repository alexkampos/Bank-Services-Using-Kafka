package org.marlow.service.impl;

import lombok.RequiredArgsConstructor;
import org.marlow.model.User;
import org.marlow.repository.UserRepository;
import org.marlow.service.UserService;
import org.marlow.util.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        Optional<User> userTryingToLogin = userRepository.findByEmail(email);
        if (userTryingToLogin.isEmpty()) {
            throw new UsernameNotFoundException(Constants.EMAIL_NOT_FOUND);
        }
        User user = userTryingToLogin.get();
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(getAuthorities())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

}
