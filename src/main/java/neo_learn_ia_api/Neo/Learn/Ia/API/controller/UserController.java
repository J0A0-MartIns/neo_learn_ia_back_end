package neo_learn_ia_api.Neo.Learn.Ia.API.controller;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.User;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
