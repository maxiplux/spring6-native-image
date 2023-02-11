package app.quantun.api.services.impl;


import app.quantun.api.config.security.JwtService;
import app.quantun.api.models.dtos.AuthenticationUserDto;
import app.quantun.api.models.dtos.AuthenticationTokenDto;
import app.quantun.api.models.dtos.RefreshTokenDto;
import app.quantun.api.models.dtos.RegisterUserDto;
import app.quantun.api.repositories.UserRepository;
import app.quantun.api.models.Role;
import app.quantun.api.models.User;
import app.quantun.api.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  @Override
  public AuthenticationTokenDto register(RegisterUserDto request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);
    return AuthenticationTokenDto.builder()
        .access_token(jwtToken)
        .build();
  }
  @Override
  public AuthenticationTokenDto authenticate(AuthenticationUserDto request) {
    authenticationManager.authenticate(        new UsernamePasswordAuthenticationToken(            request.getEmail(),            request.getPassword()        )    );
    var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateFreshToken(user);

    return AuthenticationTokenDto.builder()
            .access_token(jwtToken)
            .token_type("Bearer")
            .refresh_token(refreshToken)
        .build();
  }

  @Override
  public Long count() {
    return this.userRepository.count();
  }

  @Override
  public User save(User user) {
    return this.userRepository.save(user);
  }

  @Override
  public AuthenticationTokenDto authenticate(RefreshTokenDto request) {
    var username = jwtService.getUsernameFromToken(request.getRefreshToken());
    User user = userRepository.findByEmail(username).orElseThrow(() -> new AccessDeniedException("Invalid User not found"));

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateFreshToken(user);

    return AuthenticationTokenDto.builder()
            .access_token(jwtToken)
            .token_type("Bearer")
            .refresh_token(refreshToken)
        .build();
  }
}
