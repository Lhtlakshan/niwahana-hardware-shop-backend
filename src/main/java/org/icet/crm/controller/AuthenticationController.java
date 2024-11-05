package org.icet.crm.controller;

import lombok.RequiredArgsConstructor;
import org.icet.crm.dto.AuthenticationRequestDto;
import org.icet.crm.dto.SignupDto;
import org.icet.crm.dto.UserDto;
import org.icet.crm.entity.User;
import org.icet.crm.repository.UserRepository;
import org.icet.crm.service.auth.AuthService;
import org.icet.crm.utils.JwtUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequestDto authenticationRequestDto) throws JSONException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequestDto.getUsername());
        Optional<User> user = userRepository.findFirstByEmail(authenticationRequestDto.getUsername());
        String jwt = jwtUtil.generateToken(authenticationRequestDto.getUsername());

        if (user.isPresent()) {
            JSONObject jsonResponse = new JSONObject()
                    .put("userId", user.get().getId())
                    .put("role", user.get().getUserRole());

            HttpHeaders headers = new HttpHeaders();
            headers.set(HEADER_STRING, TOKEN_PREFIX + jwt);
            headers.add("Access-Control-Expose-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(jsonResponse.toString());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User details not found");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupDto signupDto) {
        if (userService.hasUserWithExistingEmail(signupDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        UserDto createdUser = userService.createUser(signupDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
