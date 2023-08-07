package com.aea.authservice.controller;

import com.aea.authservice.common.EmailUtils;
import com.aea.authservice.dto.request.LoginRequest;
import com.aea.authservice.dto.request.PasswordResetRequest;
import com.aea.authservice.dto.request.RegistrationRequest;
import com.aea.authservice.dto.response.AuthenticationResponse;
import com.aea.authservice.model.EmailVerificationToken;
import com.aea.authservice.model.PasswordResetToken;
import com.aea.authservice.model.User;
import com.aea.authservice.security.JwtTokenProvider;
import com.aea.authservice.service.EmailVerificationTokenService;
import com.aea.authservice.service.PasswordResetService;
import com.aea.authservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerificationTokenService tokenService;
    private final EmailUtils emailService;
    private final PasswordResetService passwordResetService;

    @GetMapping("/ping")
    public String ping() {
        return "Server is up & running!";
    }

    @Transactional
    @PostMapping("/")
    public ResponseEntity register(@Valid @RequestBody RegistrationRequest registrationRequest) throws Exception {
            if (!registrationRequest.getPassword().equals(registrationRequest.getPasswordConfirm())) {
                return new ResponseEntity<>("passwords do not match", HttpStatus.BAD_REQUEST);
            }

            if (userService.getOneByEmail(registrationRequest.getEmail()).isPresent()) {
                return new ResponseEntity<>("email already in use", HttpStatus.BAD_REQUEST);
            }

            if (userService.getOneByUsername(registrationRequest.getUsername()).isPresent()) {
                return new ResponseEntity<>("username already in use", HttpStatus.BAD_REQUEST);
            }

            User user = userService.saveUser(registrationRequest);

            // Create verification token
            EmailVerificationToken token = tokenService.createToken(user);

            // Send email to the user
            String link = "http://localhost:8080/api/v1/auth/verify?token=" + token.getToken();
            emailService.sendConfirmationEmail(registrationRequest.getEmail(), link);

            return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public String verifyToken(@RequestParam("token") String token) {
        if(jwtTokenProvider.validateToken(token)) return "true";
        else return "false"; 
    }

    @GetMapping("/verify")
    public ResponseEntity verifyAccount(@RequestParam("token") String token) throws Exception {
        EmailVerificationToken confirmationToken = tokenService.getToken(token).orElse(null);

        if(confirmationToken == null){
            return new ResponseEntity("Verification token not found!", HttpStatus.BAD_REQUEST);
        }

        if (confirmationToken.getConfirmedAt() != null) {
            return new ResponseEntity("Email already confirmed", HttpStatus.BAD_REQUEST);
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            return new ResponseEntity("Token expired. Claim a new one", HttpStatus.BAD_REQUEST);
        }

        tokenService.setConfirmedAt(token);
        userService.enableUser(confirmationToken.getUser().getEmail());

        return new ResponseEntity("Account is activated.", HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequest loginRequest) throws Exception {
        // Declare the authentication type. In our app, users authenticate with username and password
        // Some other auth types are OAuth, SAML, remember me etc.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());

        // Now find the proper authentication provider and authenticate the user
        // using the auth token
        Authentication auth = authenticationManager.authenticate(authenticationToken);

        // Then, create the JWT including required claims in it
        final String jwt = jwtTokenProvider.generateJwtToken(auth);

        // We need to get `userId`. To do so, get userDetails from authentication object
        User user = userService.getOneByUsername(loginRequest.getUsername()).get();

        return new ResponseEntity(
                new AuthenticationResponse(user.getId(), "Login successful", jwt),
                HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity forgotPassword(@RequestParam("email") String email) throws Exception {
        User user = userService.getOneByEmail(email).orElse(null);
        if(user == null) {
            return new ResponseEntity("User not found with the given email: " + email, HttpStatus.BAD_REQUEST);
        }

        // Create pw reset token
        PasswordResetToken token = passwordResetService.createToken(user);

        String url = "http://localhost:8080/api/v1/auth/reset-password?token=" + token.getToken();
        emailService.sendPwResetEmail(email, url);

        return ResponseEntity
                .ok("An email has been sent to " + email + " with further instructions. Please check your inbox.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(@RequestBody PasswordResetRequest request) throws Exception{
        PasswordResetToken resetToken = passwordResetService.getToken(request.getToken()).orElse(null);

        if(resetToken == null) return new ResponseEntity("Token not found", HttpStatus.BAD_REQUEST);

        LocalDateTime expiresAt = resetToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            return new ResponseEntity("Token expired. Claim a new one.", HttpStatus.BAD_REQUEST);
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            return new ResponseEntity("Passwords do not match", HttpStatus.BAD_REQUEST);
        }

        userService.updatePassword(resetToken.getUser().getEmail(), request.getPassword());

        return ResponseEntity.ok("Password updated successfully");
    }
}
