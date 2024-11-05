package org.icet.crm.service.auth;

import org.icet.crm.dto.SignupDto;
import org.icet.crm.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignupDto signupDto);

    boolean hasUserWithExistingEmail(String email);
}
