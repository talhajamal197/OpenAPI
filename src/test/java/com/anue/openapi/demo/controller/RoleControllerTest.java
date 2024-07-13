package com.anue.openapi.demo.controller;

import com.anue.openapi.demo.model.Role;
import com.anue.openapi.demo.service.RoleService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.anue.openapi.demo.model.Role;
import com.anue.openapi.demo.service.RoleService;
import com.anue.openapi.demo.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoles_ValidToken() {
        // Arrange
        String validToken = "validToken";
        when(authService.validateToken(validToken)).thenReturn(true);

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("Admin");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("User");

        List<Role> roles = Arrays.asList(role1, role2);
        when(roleService.findAll()).thenReturn(roles);

        // Act
        ResponseEntity<List<Role>> response = roleController.getAllRoles(validToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roles, response.getBody());
        verify(authService, times(1)).validateToken(validToken);
        verify(roleService, times(1)).findAll();
    }

    @Test
    void testGetRoleById_InvalidToken() {
        // Arrange
        String invalidToken = "invalidToken";
        when(authService.validateToken(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<Role> response = roleController.getRoleById(1L, invalidToken);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authService, times(1)).validateToken(invalidToken);
        verify(roleService, times(0)).findById(anyLong());
    }

    @Test
    void testCreateRole_ValidToken() {
        // Arrange
        String validToken = "validToken";
        when(authService.validateToken(validToken)).thenReturn(true);

        Role newRole = new Role();
        newRole.setName("NewRole");

        Role savedRole = new Role();
        savedRole.setId(1L);
        savedRole.setName("NewRole");

        when(roleService.save(any(Role.class))).thenReturn(savedRole);

        // Act
        ResponseEntity<Role> response = roleController.createRole(newRole, validToken);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedRole, response.getBody());
        verify(authService, times(1)).validateToken(validToken);
        verify(roleService, times(1)).save(newRole);
    }
}