package backend.config;

import backend.model.Fan;
import backend.repository.FanRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class InitializerConfig {

    @Autowired
    private FanRepository fanRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        String loginAdmin = "ADMIN";
        if (fanRepository.findByLogin(loginAdmin) == null) {
            Fan superAdmin = new Fan();
            superAdmin.setLogin(loginAdmin);
            superAdmin.setSenha(passwordEncoder.encode("admin"));
            superAdmin.setRole("ROLE_ADMIN");
            fanRepository.save(superAdmin);
            System.out.println("Usuário ADMIN criado com sucesso.");
        } else {
            System.out.println("Usuário ADMIN já existe.");
        }
    }
}
