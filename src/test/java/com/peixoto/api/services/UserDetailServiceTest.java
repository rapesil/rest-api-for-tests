package com.peixoto.api.services;

import com.peixoto.api.domain.Users;
import com.peixoto.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        assertThat(userDetailService.loadUserByUsername("username")).isNotNull();
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException() {
        assertThatThrownBy(() -> userDetailService.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User username not found");
    }

}
