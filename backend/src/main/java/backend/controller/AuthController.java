package backend.controller;

import backend.DTO.AuthRequest;
import backend.model.Fan;
import backend.repository.FanRepository;
import backend.security.TokenService;
import backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;
    @Autowired
    private FanRepository fanRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {


        String token = authService.register(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest request) {

        try {

            String login = request.login();
            String senha = request.senha();

            Authentication authentication = autenticar(login, senha);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Fan fan = fanRepository.findByLogin(login);

            String token = tokenService.generateToken(fan);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        }
    }

    private Authentication autenticar(String login, String senha) {
        Fan fan = authService.obterPorLogin(login);
        if (fan == null) throw new UsernameNotFoundException("Usuario não encontrado");

        if (!passwordEncoder.matches(senha, fan.getSenha())) {
            throw new BadCredentialsException("Senha inválida");
        }

        return new UsernamePasswordAuthenticationToken(fan.getLogin(), null, null);
    }

}

