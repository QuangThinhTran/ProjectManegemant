package com.vn.projectmanagement.factory;

import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserFactory(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User create(Role role) {

        User user = new User();
        user.setUsername(RandomStringUtils.randomAlphanumeric(10));
        user.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@example.com");
        user.setPhone(RandomStringUtils.randomNumeric(10));
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(role);
        return userRepository.save(user);
    }
}
