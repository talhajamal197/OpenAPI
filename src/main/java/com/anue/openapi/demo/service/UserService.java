package com.anue.openapi.demo.service;


import com.anue.openapi.demo.model.Role;
import com.anue.openapi.demo.model.User;
import com.anue.openapi.demo.repository.RoleRepository;
import com.anue.openapi.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    public Optional<User> assignRolesToUser(Long userId, List<Long> roleIds) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        Set<Role> existingRoles = user.getRoles();
        List<Role> rolesToAdd = roleRepository.findAllById(roleIds).stream()
                .filter(role -> !existingRoles.contains(role))
                .collect(Collectors.toList());

        existingRoles.addAll(rolesToAdd);
        user.setRoles(existingRoles);
        return Optional.of(userRepository.save(user));
    }
}
