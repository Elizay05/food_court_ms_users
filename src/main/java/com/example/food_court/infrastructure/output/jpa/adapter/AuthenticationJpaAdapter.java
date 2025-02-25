package com.example.food_court.infrastructure.output.jpa.adapter;

import com.example.food_court.domain.model.Authentication;
import com.example.food_court.domain.model.Token;
import com.example.food_court.domain.spi.IAuthenticationPersistencePort;
import com.example.food_court.infrastructure.configuration.security.jwt.JwtService;
import com.example.food_court.infrastructure.exception.IncorrectCredentialsException;
import com.example.food_court.infrastructure.output.jpa.entity.UserEntity;
import com.example.food_court.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationJpaAdapter implements IAuthenticationPersistencePort {

    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public Token authenticate(Authentication authentication) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authentication.getEmail(),
                            authentication.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new IncorrectCredentialsException("Credenciales incorrectas");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getEmail());
        UserEntity userEntity = userRepository.findByEmail(authentication.getEmail())
                .orElseThrow(() -> new IncorrectCredentialsException("Usuario no encontrado"));

        String documentNumber = userEntity.getDocumentNumber();
        String nit = userEntity.getNit();
        String jwtToken = jwtService.generateToken(userDetails, documentNumber, nit);
        return new Token(jwtToken);
    }
}