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

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRolesAndRoleById() {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("Admin");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("User");

        List<Role> roles = Arrays.asList(role1, role2);
        when(roleService.findAll()).thenReturn(roles);
        when(roleService.findById(1L)).thenReturn(Optional.of(role1));
        when(roleService.findById(3L)).thenReturn(Optional.empty());

        // Test getAllRoles
        List<Role> result = roleController.getAllRoles();
        assertEquals(2, result.size());
        assertEquals("Admin", result.get(0).getName());
        assertEquals("User", result.get(1).getName());
        verify(roleService, times(1)).findAll();

        // Test getRoleById
        ResponseEntity<Role> response = roleController.getRoleById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Admin", response.getBody().getName());
        verify(roleService, times(1)).findById(1L);

        // Test getRoleById not found
        response = roleController.getRoleById(3L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(roleService, times(1)).findById(3L);
    }

    @Test
    void testCreateAndUpdateRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        Role updatedRoleDetails = new Role();
        updatedRoleDetails.setId(1L);
        updatedRoleDetails.setName("Super Admin");

        when(roleService.save(any(Role.class))).thenReturn(role);
        when(roleService.findById(1L)).thenReturn(Optional.of(role));
        when(roleService.save(any(Role.class))).thenReturn(updatedRoleDetails);
        when(roleService.findById(2L)).thenReturn(Optional.empty());

        // Test createRole
        Role result = roleController.createRole(role);
        assertEquals("Super Admin", result.getName());
        verify(roleService, times(1)).save(any(Role.class));

        // Test updateRole
        ResponseEntity<Role> response = roleController.updateRole(1L, updatedRoleDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Super Admin", response.getBody().getName());

        // Test updateRole not found
        response = roleController.updateRole(2L, updatedRoleDetails);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(roleService, times(1)).findById(2L);
    }

    @Test
    void testDeleteRole() {
        when(roleService.findById(1L)).thenReturn(Optional.of(new Role()));
        when(roleService.findById(2L)).thenReturn(Optional.empty());

        // Test deleteRole
        ResponseEntity<Void> deleteResponse = roleController.deleteRole(1L);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        verify(roleService, times(1)).deleteById(1L);

        // Test deleteRole not found
        deleteResponse = roleController.deleteRole(2L);
        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
        verify(roleService, times(1)).findById(2L);
    }
}
