package app.quantun.api.controller.auth;

import app.quantun.api.models.dtos.AuthenticationUserDto;
import app.quantun.api.models.dtos.AuthenticationTokenDto;
import app.quantun.api.models.dtos.RefreshTokenDto;
import app.quantun.api.models.dtos.RegisterUserDto;
import app.quantun.api.services.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationServiceImpl service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationTokenDto> register(
      @RequestBody RegisterUserDto request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/login")
  public ResponseEntity<AuthenticationTokenDto> authenticate(@RequestBody AuthenticationUserDto request) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthenticationTokenDto> refreshToken(@RequestBody RefreshTokenDto request) {
    return ResponseEntity.ok(service.authenticate(request));
  }




}
