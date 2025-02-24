package com.example.food_court.infrastructure.configuration;

import com.example.food_court.domain.api.IAuthenticationServicePort;
import com.example.food_court.domain.api.IUserServicePort;
import com.example.food_court.domain.spi.IAuthenticationPersistencePort;
import com.example.food_court.domain.spi.IPasswordEncryptionPort;
import com.example.food_court.domain.spi.IUserPersistencePort;
import com.example.food_court.domain.usecase.AuthenticationUseCase;
import com.example.food_court.domain.usecase.UserCase;
import com.example.food_court.infrastructure.configuration.security.jwt.JwtService;
import com.example.food_court.infrastructure.output.jpa.adapter.AuthenticationAdapter;
import com.example.food_court.infrastructure.output.jpa.adapter.UserJpaAdapter;
import com.example.food_court.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.example.food_court.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.example.food_court.infrastructure.output.jpa.repository.IRoleRepository;
import com.example.food_court.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;
    private final IRoleRepository rolRepository;
    private final IRoleEntityMapper entityMapper;
    private final IPasswordEncryptionPort passwordEncryptionPort;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userEntityMapper, userRepository,rolRepository, entityMapper);
    }

    @Bean
    public IUserServicePort userServicePort() {
        return new UserCase(userPersistencePort(), passwordEncryptionPort);
    }

    @Bean
    public IAuthenticationPersistencePort authenticationPersistencePort() {
        return new AuthenticationAdapter(userRepository, jwtService, authenticationManager, userDetailsService);
    }
    @Bean
    public IAuthenticationServicePort authenticationServicePort() {
        return new AuthenticationUseCase(authenticationPersistencePort());
    }
}
