package tech.itpark.service;

import tech.itpark.dto.user.*;
import tech.itpark.security.Auth;
import tech.itpark.security.AuthProvider;

public interface UserService extends AuthProvider {
    @Override
    Auth provide(String token);

    RegistrationResponseDto register(RegistrationRequestDto request);

    UnregisterResponseDto delete(UnregisterRequestDto requestDto, String token);

    RestoreResponseDto restore(RestoreRequestDto requestDto);

    LoginResponseDto login(LoginRequestDto request);

    LogoutResponseDto logout(Auth auth, String token);

    UpdatePasswordResponseDto updatePassword(UpdatePasswordRequestDto requestDto);

    UpdateSecretResponseDto updateSecret(UpdateSecretRequestDto requestDto);
}
