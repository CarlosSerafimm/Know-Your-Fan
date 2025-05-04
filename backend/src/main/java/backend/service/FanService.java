package backend.service;

import backend.DTO.RequestFanDTO;
import backend.DTO.ResponseFanDTO;
import backend.model.Fan;
import backend.model.enums.*;
import backend.model.mapper.FanMapper;
import backend.repository.FanRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class FanService {

    @Autowired
    private FanRepository fanRepository;
    @Autowired
    private FanMapper fanMapper;
    @Autowired
    private Dotenv dotenv;

    public ResponseFanDTO getFan() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        Fan Teste = (Fan) authentication.getPrincipal();
        Fan fan = fanRepository.findByLogin(Teste.getLogin());
        return fanMapper.entityToResponseFan(fan);
    }

    public List<Fan> getAll() {
        return fanRepository.findAll();
    }

    public ResponseFanDTO updateLoggedFan(RequestFanDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Fan Teste = (Fan) authentication.getPrincipal();

        Fan fan = fanRepository.findByLogin(Teste.getLogin());
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

    public void vincularContaTwitch(String login, String twitchId, String twitchLogin, String twitchName, String accessToken) {
        Fan fan = fanRepository.findByLogin(login);
        if (fan == null) throw new RuntimeException("Usuário não encontrado");

        fan.setTwitchId(twitchId);
        fan.setTwitchLogin(twitchLogin);
        fan.setTwitchName(twitchName);
        fan.setSegueFuria(usuarioSegueFuria( accessToken,twitchId));
        System.out.println("canal da furia");
        System.out.println(obterIdCanalFuria(accessToken));
        fanRepository.save(fan);
    }

    public String obterIdCanalFuria(String accessToken) {
        // URL da API da Twitch para buscar o usuário (canal) com o nome fixo "furia"
        String url = "https://api.twitch.tv/helix/users?login=FURIAtv";

        String clientId = dotenv.get("TWITCH_CLIENT_ID");

        // Configuração de cabeçalhos
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);  // Token de acesso necessário para autenticação
        headers.set("Client-Id", clientId);  // Seu Client ID da Twitch

        // Preparando a requisição
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Usando RestTemplate para fazer a requisição
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao buscar o ID do canal da FURIA");
        }

        // Processando a resposta
        Map responseBody = response.getBody();
        if (responseBody == null || responseBody.get("data") == null) {
            throw new RuntimeException("Canal da FURIA não encontrado");
        }

        // Extraindo o ID do canal da FURIA
        List<Map<String, Object>> userData = (List<Map<String, Object>>) responseBody.get("data");
        if (userData.isEmpty()) {
            throw new RuntimeException("Canal da FURIA não encontrado");
        }

        // O ID do canal da FURIA
        String channelId = (String) userData.get(0).get("id");
        return channelId;
    }

//    public boolean usuarioSegueFuria(String twitchUserId, String accessToken) {
//        String clientId = dotenv.get("TWITCH_CLIENT_ID");
//        String furiaChannelId = "178500702";
//
//        String url = String.format(
//                "https://api.twitch.tv/helix/users/follows?from_id=%s&to_id=%s",
//                twitchUserId, furiaChannelId
//        );
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        headers.add("Client-Id", clientId);
//
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//
//        ResponseEntity<Map> response = restTemplate.exchange(
//                url, HttpMethod.GET, request, Map.class
//        );
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            Map<String, Object> body = response.getBody();
//            Integer total = (Integer) body.get("total");
//            return total != null && total > 0;
//        }
//
//        return false;
//    }

    public boolean usuarioSegueFuria(String accessToken, String userId) {
        String clientId = dotenv.get("TWITCH_CLIENT_ID");
        String furiaChannelId = "178500702";

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.twitch.tv/helix/channels/followed?user_id=" + userId + "&broadcaster_id="+ furiaChannelId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.add("Client-Id", clientId);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<?> data = (List<?>) response.getBody().get("data");
            return data != null && !data.isEmpty();
        }

        return false;
    }



}
