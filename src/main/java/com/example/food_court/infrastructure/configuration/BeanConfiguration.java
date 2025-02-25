package com.example.food_court.infrastructure.configuration;

import com.example.food_court.domain.api.IAuthenticationServicePort;
import com.example.food_court.domain.api.IUserServicePort;
import com.example.food_court.domain.spi.IAuthenticationPersistencePort;
import com.example.food_court.domain.spi.IPasswordEncryptionPort;
import com.example.food_court.domain.spi.ISmallSquarePersistencePort;
import com.example.food_court.domain.spi.IUserPersistencePort;
import com.example.food_court.domain.usecase.AuthenticationUseCase;
import com.example.food_court.domain.usecase.UserCase;
import com.example.food_court.infrastructure.configuration.security.jwt.JwtService;
import com.example.food_court.infrastructure.output.jpa.adapter.AuthenticationJpaAdapter;
import com.example.food_court.infrastructure.output.jpa.adapter.UserJpaAdapter;
import com.example.food_court.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.example.food_court.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.example.food_court.infrastructure.output.jpa.repository.IRoleRepository;
import com.example.food_court.infrastructure.output.jpa.repository.IUserRepository;
import com.example.food_court.infrastructure.output.rest.SmallSquareRestAdapter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.RestTemplate;

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
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ISmallSquarePersistencePort smallSquarePersistencePort(RestTemplate restTemplate, HttpServletRequest httpServletRequest){
        return new SmallSquareRestAdapter(restTemplate, httpServletRequest);
    }

    @Bean
    public IUserServicePort userServicePort(ISmallSquarePersistencePort smallSquarePersistencePort) {
        return new UserCase(userPersistencePort(), passwordEncryptionPort, smallSquarePersistencePort);
    }

    @Bean
    public IAuthenticationPersistencePort authenticationPersistencePort() {
        return new AuthenticationJpaAdapter(userRepository, jwtService, authenticationManager, userDetailsService);
    }
    @Bean
    public IAuthenticationServicePort authenticationServicePort() {
        return new AuthenticationUseCase(authenticationPersistencePort());
    }
}
