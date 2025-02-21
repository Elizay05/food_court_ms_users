package com.example.food_court.infrastructure.configuration;

import com.example.food_court.domain.api.IUserServicePort;
import com.example.food_court.domain.spi.IPasswordEncryptionPort;
import com.example.food_court.domain.spi.IUserPersistencePort;
import com.example.food_court.domain.usecase.UserCase;
import com.example.food_court.infrastructure.output.jpa.adapter.UserJpaAdapter;
import com.example.food_court.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.example.food_court.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.example.food_court.infrastructure.output.jpa.repository.IRoleRepository;
import com.example.food_court.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;
    private final IRoleRepository rolRepository;
    private final IRoleEntityMapper entityMapper;
    private final IPasswordEncryptionPort passwordEncryptionPort;

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userEntityMapper, userRepository,rolRepository, entityMapper);
    }

    @Bean
    public IUserServicePort userServicePort() {
        return new UserCase(userPersistencePort(), passwordEncryptionPort);
    }
}
