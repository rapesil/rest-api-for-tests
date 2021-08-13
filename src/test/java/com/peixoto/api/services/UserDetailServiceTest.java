package com.peixoto.api.services;

import com.peixoto.api.domain.Users;
import com.peixoto.api.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceTest {

    @InjectMocks
    private UserDetailService userDetailService;

    @Mock
    private UserRepository mockUserRepository;

    @Test
    void loadUserByUsername_shouldReturnUsername() {
        Mockito.when(mockUserRepository.findByUsername(Mockito.anyString()))
                .thenReturn(new Users());

        Assertions.assertThat(userDetailService.loadUserByUsername("username")).isNotNull();
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException() {
        Assertions.assertThatThrownBy(() -> userDetailService.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User username not found");
    }

}
