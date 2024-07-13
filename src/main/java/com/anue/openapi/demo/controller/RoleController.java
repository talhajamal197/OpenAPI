package com.anue.openapi.demo.controller;

import com.anue.openapi.demo.model.Role;
import com.anue.openapi.demo.service.RoleService;
import com.anue.openapi.demo.auth.AuthService; // Import AuthService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService; // Injecting AuthService

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles(@RequestHeader("Authorization") String authHeader) {
        if (authService.validateToken(authHeader)) {
            List<Role> roles = roleService.findAll();
            return ResponseEntity.ok(roles);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        if (authService.validateToken(authHeader)) {
            Optional<Role> role = roleService.findById(id);
            return role.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@Validated @RequestBody Role role, @RequestHeader("Authorization") String authHeader) {
        if (authService.validateToken(authHeader)) {
            Role savedRole = roleService.save(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @Validated @RequestBody Role roleDetails, @RequestHeader("Authorization") String authHeader) {
        if (authService.validateToken(authHeader)) {
            Optional<Role> role = roleService.findById(id);
            if (role.isPresent()) {
                Role updatedRole = role.get();
                updatedRole.setName(roleDetails.getName());
                roleService.save(updatedRole);
                return ResponseEntity.ok(updatedRole);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        if (authService.validateToken(authHeader)) {
            if (roleService.findById(id).isPresent()) {
                roleService.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
