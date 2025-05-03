package backend.model;

import backend.model.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fan implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String senha;

    private String nomeCompleto;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @ElementCollection
    private List<Jogo> jogosFavoritos;

    @ElementCollection
    private List<Evento> eventosParticipados;

    @ElementCollection
    private List<Produto> produtosComprados;

    @ElementCollection
    private List<Jogador> jogadoresFavoritos;

    @ElementCollection
    private List<Plataforma> plataformasAssistidas;


    @ElementCollection
    private List<RedeSocial> redesSeguidas;


    private String twitter;
    private String instagram;
    private String linkedIn;

    private boolean validado = false;
    private int pontuacao;


    private String twitchId;
    private String twitchLogin;
    private String twitchName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
