package neo_learn_ia_api.Neo.Learn.Ia.API.repository;

import neo_learn_ia_api.Neo.Learn.Ia.API.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByUserIdAndType(Long userId, VerificationToken.TokenType type);
}