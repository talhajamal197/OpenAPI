package com.anue.openapi.demo.service;

import com.anue.openapi.demo.model.Role;
import com.anue.openapi.demo.model.User;
import com.anue.openapi.demo.repository.RoleRepository;
import com.anue.openapi.demo.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAssignRolesToUser() {
        // Prepare mock data
        User user = createUser(1L, "talha jamal", "talhajamal@gmail.com");
        Role role1 = createRole(1L, "ROLE_USER");
        Role role2 = createRole(2L, "ROLE_ADMIN");
        List<Role> existingRoles = new ArrayList<>(Arrays.asList(role1));
        user.setRoles(new HashSet<>(existingRoles));

        List<Long> roleIdsToAdd = Arrays.asList(1L, 2L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findAllById(roleIdsToAdd)).thenReturn(Arrays.asList(role1, role2));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Call the method under test
        Optional<User> updatedUser = userService.assignRolesToUser(1L, roleIdsToAdd);

        // Assertions
        Assertions.assertTrue(updatedUser.isPresent());
        Set<Role> updatedRoles = updatedUser.get().getRoles();
        Assertions.assertEquals(2, updatedRoles.size());
        Assertions.assertTrue(updatedRoles.contains(role1));
        Assertions.assertTrue(updatedRoles.contains(role2));
    }

    @Test
    public void testAssignRolesToUser_UserNotFound() {
        // Prepare mock data
        List<Long> roleIdsToAdd = Arrays.asList(1L, 2L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the method under test
        Optional<User> updatedUser = userService.assignRolesToUser(1L, roleIdsToAdd);

        // Assertions
        Assertions.assertFalse(updatedUser.isPresent());
    }

    // Helper method to create Role objects
    private Role createRole(Long id, String name) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        return role;
    }

    // Helper method to create User objects
    private User createUser(Long id, String username, String email) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }
}
