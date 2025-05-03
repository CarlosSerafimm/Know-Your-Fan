package backend.service;

import backend.DTO.RequestFanDTO;
import backend.DTO.ResponseFanDTO;
import backend.model.Fan;
import backend.model.enums.*;
import backend.model.mapper.FanMapper;
import backend.repository.FanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FanService {

    @Autowired
    private FanRepository fanRepository;
    @Autowired
    private FanMapper fanMapper;

    public ResponseFanDTO getFan() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String login = (String) authentication.getPrincipal();
        Fan fan = fanRepository.findByLogin(login);
        return fanMapper.entityToResponseFan(fan);
    }

    public List<Fan> getAll() {
        return fanRepository.findAll();
    }

    public ResponseFanDTO updateLoggedFan(RequestFanDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = (String) authentication.getPrincipal();

        Fan fan = fanRepository.findByLogin(login);
        if (fan == null) new RuntimeException("Usuário não encontrado");


        fanMapper.updateEntityFromDto(dto, fan);

        fan.setPontuacao(calcularPontos(fan));
        fanRepository.save(fan);
        return fanMapper.entityToResponseFan(fan);
    }

    public int calcularPontos(Fan fan) {

        int jogos = Jogo.values().length - 1;
        int evento = Evento.values().length - 1;
        int produto = Produto.values().length;
        int jogador = Jogador.values().length;
        int redeSocial = RedeSocial.values().length - 1;
        int plataforma = Plataforma.values().length - 1;

        int totalPontos = jogos + evento + produto + jogador + redeSocial + plataforma;
        int pontos = 0;

        if (fan.getJogosFavoritos() != null) pontos += fan.getJogosFavoritos().size() - 1;
        if (fan.getEventosParticipados() != null) pontos += fan.getEventosParticipados().size() - 1;
        if (fan.getProdutosComprados() != null) pontos += fan.getProdutosComprados().size();
        if (fan.getJogadoresFavoritos() != null) pontos += fan.getJogadoresFavoritos().size();
        if (fan.getRedesSeguidas() != null) pontos += fan.getRedesSeguidas().size() - 1;
        if (fan.getPlataformasAssistidas() != null) pontos += fan.getPlataformasAssistidas().size() - 1;

        return (pontos * 100) / totalPontos;
    }

    public void vincularContaTwitch(String login, String twitchId, String twitchLogin, String twitchName) {
        Fan fan = fanRepository.findByLogin(login);
        if (fan == null) throw new RuntimeException("Usuário não encontrado");

        fan.setTwitchId(twitchId);
        fan.setTwitchLogin(twitchLogin);
        fan.setTwitchName(twitchName);
        fanRepository.save(fan);
    }

}
