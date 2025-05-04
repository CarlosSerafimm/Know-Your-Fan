package backend.service;

import backend.DTO.AuthRequest;
import backend.model.Fan;
import backend.repository.FanRepository;
import backend.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private FanRepository fanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public String register(AuthRequest authRequest) {
        Fan existente = fanRepository.findByLogin(authRequest.login());
        if (existente != null) throw new RuntimeException("Usuario já existe");

        Fan fan = new Fan();
        fan.setLogin(authRequest.login());
        fan.setSenha(passwordEncoder.encode(authRequest.senha()));
        fan.setRole("ROLE_USER");
        fanRepository.save(fan);
        return tokenService.generateToken(fan);
    }

    public Fan obterPorLogin(String login){
        return fanRepository.findByLogin(login);
    }

}