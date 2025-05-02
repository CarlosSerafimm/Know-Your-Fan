package backend.repository;

import backend.model.Fan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FanRepository extends JpaRepository<Fan, Long> {
    Fan findByLogin(String login);
}
