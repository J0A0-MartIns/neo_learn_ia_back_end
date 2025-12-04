package neo_learn_ia_api.Neo.Learn.Ia.API.controller;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.User;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.UpdateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.UserDto;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.*;

@RestController
@RequestMapping( "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<User>> getAllUsers() {
        var users = userService.listUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_BASIC') or hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserDto user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_BASIC') or hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> updateMe(@AuthenticationPrincipal Jwt jwt, @RequestBody UpdateUserDto updateUserDto) {
        Long userId = Long.parseLong(jwt.getSubject());
        userService.updateUser(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/email")
    @PreAuthorize("hasAuthority('SCOPE_BASIC') or hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> updateEmail(@AuthenticationPrincipal Jwt jwt, @RequestBody Map<String, String> payload) {
        Long userId = Long.parseLong(jwt.getSubject());
        String newEmail = payload.get("email");

        userService.updateEmail(userId, newEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        userService.confirmEmail(token);
        return ResponseEntity.ok("Email confirmado com sucesso!");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        userService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam("token") String token, @RequestBody Map<String, String> payload) {
        String newPassword = payload.get("password");
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }
}
