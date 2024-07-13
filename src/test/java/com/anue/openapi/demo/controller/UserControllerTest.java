package com.anue.openapi.demo.controller;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsersAndUserById() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Talha Jamal");
        user1.setEmail("talhajamal@gmail.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Viktor Krawutschke");
        user2.setEmail("viktor.krawutschke@googlemail.com");

        List<User> users = Arrays.asList(user1, user2);
        when(userService.findAll()).thenReturn(users);
        when(userService.findById(1L)).thenReturn(Optional.of(user1));
        when(userService.findById(3L)).thenReturn(Optional.empty());

        // Test getAllUsers
        List<User> result = userController.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("Talha Jamal", result.get(0).getUsername());
        assertEquals("Viktor Krawutschke", result.get(1).getUsername());
        verify(userService, times(1)).findAll();

        // Test getUserById
        ResponseEntity<User> response = userController.getUserById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Talha Jamal", response.getBody().getUsername());
        assertEquals("talhajamal@gmail.com", response.getBody().getEmail());
        verify(userService, times(1)).findById(1L);

        // Test getUserById not found
        response = userController.getUserById(3L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).findById(3L);
    }

    @Test
    void testCreateAndUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Talha Jamal");
        user.setEmail("talhajamal@gmail.com");

        User updatedUserDetails = new User();
        updatedUserDetails.setId(1L);
        updatedUserDetails.setUsername("Updated Talha Jamal");
        updatedUserDetails.setEmail("updated.talhajamal@gmail.com");

        when(userService.save(any(User.class))).thenReturn(user);
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(userService.save(any(User.class))).thenReturn(updatedUserDetails);
        when(userService.findById(2L)).thenReturn(Optional.empty());

        // Test createUser
        User result = userController.createUser(user);
        assertEquals("Updated Talha Jamal", result.getUsername());
        assertEquals("updated.talhajamal@gmail.com", result.getEmail());
        verify(userService, times(1)).save(any(User.class));

        // Test updateUser
        ResponseEntity<User> response = userController.updateUser(1L, updatedUserDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Talha Jamal", response.getBody().getUsername());
        assertEquals("updated.talhajamal@gmail.com", response.getBody().getEmail());

        // Test updateUser not found
        response = userController.updateUser(2L, updatedUserDetails);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).findById(2L);
    }

    @Test
    void testDeleteAndAssignRolesToUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Talha Jamal");
        user.setEmail("talhajamal@gmail.com");

        List<Long> roleIds = Arrays.asList(1L, 2L);

        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(userService.findById(2L)).thenReturn(Optional.empty());
        when(userService.assignRolesToUser(1L, roleIds)).thenReturn(Optional.of(user));
        when(userService.assignRolesToUser(2L, roleIds)).thenReturn(Optional.empty());

        // Test deleteUser
        ResponseEntity<Void> deleteResponse = userController.deleteUser(1L);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        verify(userService, times(1)).deleteById(1L);

        // Test deleteUser not found
        deleteResponse = userController.deleteUser(2L);
        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
        verify(userService, times(1)).findById(2L);

        // Test assignRolesToUser
        ResponseEntity<User> response = userController.assignRolesToUser(1L, roleIds);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Talha Jamal", response.getBody().getUsername());
        assertEquals("talhajamal@gmail.com", response.getBody().getEmail());
        verify(userService, times(1)).assignRolesToUser(1L, roleIds);

        // Test assignRolesToUser not found
        response = userController.assignRolesToUser(2L, roleIds);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).assignRolesToUser(2L, roleIds);
    }
}
