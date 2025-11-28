package neo_learn_ia_api.Neo.Learn.Ia.API.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.LoginRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.validation.annotations.DomainNotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name= "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @DomainNotNull(message = "Email não pode ser nulo")
    @Column(unique = true)
    private String userEmail;

    @DomainNotNull(message = "Senha não poder ser nulo")
    private String password;

    @DomainNotNull(message = "O primeiro nome não pode ser nulo")
    private String userFirstName;

    private String telefone;
    private String cargo;
    private String instituicao;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean emailVerificado;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;


    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.password(), this.password);
    }


    public enum Values {
        ADMIN(1L),
        BASIC(2L);

        long roleId;

        Values(long roleId) {
            this.roleId = roleId;
        }

        public long getRoleId() {
            return roleId;
        }
    }

}