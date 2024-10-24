package com.minguard.service.impl;

import com.minguard.dto.user.PasswordValidation;
import com.minguard.dto.user.RegisterUserRequest;
import com.minguard.dto.user.RegisterUserResponse;
import com.minguard.dto.user.UpdateUserRequest;
import com.minguard.dto.user.UserResponse;
import com.minguard.entity.Gender;
import com.minguard.entity.Role;
import com.minguard.entity.User;
import com.minguard.mapper.UserMapper;
import com.minguard.repository.UserRepository;
import com.minguard.service.spec.GenderService;
import com.minguard.service.spec.RoleService;
import com.minguard.service.spec.UserService;
import com.minguard.util.Roles;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final GenderService genderService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long getAuthenticatedUserId() {
        Authentication authentication = getAuthentication();

        return ((User) authentication.getPrincipal()).getId();
    }

    @Override
    public UserResponse getAuthenticatedUser() {
        Authentication authentication = getAuthentication();

        return UserMapper.INSTANCE.toResponse((User) authentication.getPrincipal());
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }
        return authentication;
    }

    @Override
    public List<UserResponse> findAll() {
        return UserMapper.INSTANCE.toResponses(userRepository.findAll());
    }

    public List<UserResponse> getAllUsersByRole(Roles roleName) {
        Role role = roleService.getByName(roleName);

        List<User> users = userRepository.findByRole(role);
        return UserMapper.INSTANCE.toResponses(users);
    }

    private void assignRole(User user, Roles roleName) {
        Role role = roleService.getByName(roleName);
        user.setRole(role);
    }

    private void assignGender(User user, Long genderId) {
        Gender gender = genderService.getById(genderId);
        user.setGender(gender);
    }

    private void assignPassword(User user, String password) {
        final var encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
    }

    @Override
    public RegisterUserResponse register(RegisterUserRequest request, Roles roleName) {
        assertPasswordsMatch(request);

        User user = UserMapper.INSTANCE.fromRegisterRequest(request);

        assignPassword(user, request.getPassword());
        assignGender(user, request.getGenderId());
        assignRole(user, roleName);

        return UserMapper.INSTANCE.toRegisterResponse(userRepository.save(user));
    }

    private void assertPasswordsMatch(PasswordValidation request) {
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }
    }

    @Override
    public UserResponse uploadProfilePicture(MultipartFile file) {
        throw new NotImplementedException("Not implemented yet");
    }

    
    public UserResponse getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return UserMapper.INSTANCE.toResponse(user.get());
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public UserResponse editUser(Long userId, UpdateUserRequest request) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        UserMapper.INSTANCE.updateUserPartial(user,request);

        if (request.getPassword() != null && request.getPasswordConfirm() != null) {
            assertPasswordsMatch(request);
            assignPassword(user, request.getPassword());
        }

        if (request.getGenderId() != null) {
            assignGender(user, request.getGenderId());
        }

        return UserMapper.INSTANCE.toResponse(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(userId);
    }
}
