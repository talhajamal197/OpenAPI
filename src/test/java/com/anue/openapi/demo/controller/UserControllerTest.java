package com.anue.openapi.demo.controller;
import com.anue.openapi.demo.auth.AuthService;
import com.anue.openapi.demo.controller.UserController;
import com.anue.openapi.demo.model.User;
import com.anue.openapi.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsersAuthorized() {
        when(authService.validateToken(any())).thenReturn(true);
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers("validToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testGetAllUsersUnauthorized() {
        when(authService.validateToken(any())).thenReturn(false);

        ResponseEntity<List<User>> response = userController.getAllUsers("invalidToken");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetUserByIdAuthorized() {
        when(authService.validateToken(any())).thenReturn(true);
        User user = new User();
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(1L, "validToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
}
