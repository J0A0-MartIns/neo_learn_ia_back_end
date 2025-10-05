package neo_learn_ia_api.Neo.Learn.Ia.API.config;

import jakarta.transaction.Transactional;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.Role;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.User;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.RoleRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig  implements CommandLineRunner {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AdminUserConfig(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var adminRole = roleRepository.findByName(User.Values.ADMIN.name());
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName(User.Values.ADMIN.name());
            roleRepository.save(adminRole);
        }

        var basicRole = roleRepository.findByName(User.Values.BASIC.name());
        if (basicRole == null) {
            basicRole = new Role();
            basicRole.setName(User.Values.BASIC.name());
            roleRepository.save(basicRole);
        }

        final Role finalAdminRole = adminRole;
        var userAdmin = userRepository.findByUserEmail("admin");

        userAdmin.ifPresentOrElse(
                user -> System.out.println("admin ja existe"),
                () -> {
                    var user = new User();
                    user.setUserEmail("admin");
                    user.setPassword(passwordEncoder.encode("#neo321"));
                    user.setRoles(Set.of(finalAdminRole));
                    user.setUserFirstName("admin");
                    userRepository.save(user);
                    System.out.println("admin criado com sucesso");
                }
        );
    }
}