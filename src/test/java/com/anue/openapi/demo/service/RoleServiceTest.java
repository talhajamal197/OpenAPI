package com.anue.openapi.demo.service;

import com.anue.openapi.demo.model.Role;
import com.anue.openapi.demo.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllRoles() {
        // Prepare data
        List<Role> roles = new ArrayList<>();
        roles.add(createRole(1L, "ROLE_USER"));
        roles.add(createRole(2L, "ROLE_ADMIN"));

        // Mock behavior
        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        // Call service method
        List<Role> returnedRoles = roleService.findAll();

        // Assertions
        Assertions.assertEquals(2, returnedRoles.size());
        Assertions.assertEquals("ROLE_USER", returnedRoles.get(0).getName());
        Assertions.assertEquals("ROLE_ADMIN", returnedRoles.get(1).getName());
    }

    @Test
    public void testFindRoleById() {
        // Prepare data
        Role role = createRole(1L, "ROLE_USER");

        // Mock behavior
        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Call service method
        Optional<Role> returnedRole = roleService.findById(1L);

        // Assertions
        Assertions.assertTrue(returnedRole.isPresent());
        Assertions.assertEquals("ROLE_USER", returnedRole.get().getName());
    }

    @Test
    public void testSaveRole() {
        // Prepare data
        Role roleToSave = createRole(null, "ROLE_MANAGER");
        Role savedRole = createRole(1L, "ROLE_MANAGER");

        // Mock behavior
        Mockito.when(roleRepository.save(roleToSave)).thenReturn(savedRole);

        // Call service method
        Role returnedRole = roleService.save(roleToSave);

        // Assertions
        Assertions.assertNotNull(returnedRole.getId());
        Assertions.assertEquals("ROLE_MANAGER", returnedRole.getName());
    }

    @Test
    public void testDeleteRoleById() {
        // Mock behavior
        Mockito.doNothing().when(roleRepository).deleteById(1L);

        // Call service method
        roleService.deleteById(1L);

        // Verify repository method was called
        Mockito.verify(roleRepository, Mockito.times(1)).deleteById(1L);
    }

    // Helper method to create Role objects
    private Role createRole(Long id, String name) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        return role;
    }
}
