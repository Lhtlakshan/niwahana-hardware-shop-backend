package org.icet.crm.service.auth.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.SignupDto;
import org.icet.crm.dto.UserDto;
import org.icet.crm.entity.User;
import org.icet.crm.enums.UserRole;
import org.icet.crm.repository.UserRepository;
import org.icet.crm.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(SignupDto signupDto) {
        User user = new User();
        user.setName(signupDto.getName());
        user.setEmail(signupDto.getEmail());
        user.setUserRole(UserRole.CUSTOMER);
        user.setPassword(new BCryptPasswordEncoder().encode(signupDto.getPassword()));
        userRepository.save(user);

        return user.mapUserToUserDto();
    }

    @Override
    public boolean hasUserWithExistingEmail(String email) {
        return (userRepository.findFirstByEmail(email).isPresent());
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByUserRole(UserRole.ADMIN);

        if(null == adminAccount){
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setUserRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }
    }
}
