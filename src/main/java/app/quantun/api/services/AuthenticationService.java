package app.quantun.api.services;

import app.quantun.api.models.User;
import app.quantun.api.models.dtos.AuthenticationTokenDto;
import app.quantun.api.models.dtos.AuthenticationUserDto;
import app.quantun.api.models.dtos.RefreshTokenDto;
import app.quantun.api.models.dtos.RegisterUserDto;

public interface AuthenticationService {
    AuthenticationTokenDto register(RegisterUserDto request);

    AuthenticationTokenDto authenticate(AuthenticationUserDto request);

    Long count();

    User save(User user);

    AuthenticationTokenDto authenticate(RefreshTokenDto request);
}
