package backend.controller;

import backend.security.TokenService;
import backend.service.FanService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oauth/twitch")
@RequiredArgsConstructor
public class TwitchOAuthController {



    Dotenv dotenv = Dotenv.configure().load();
    String clientId = dotenv.get("TWITCH_CLIENT_ID");
    String clientSecret = dotenv.get("TWITCH_CLIENT_SECRET");
    String redirectUri = dotenv.get("TWITCH_REDIRECT_URI");

    @Autowired
    private FanService fanService;
    @Autowired
    private TokenService tokenService;

    @GetMapping("/callback")
    public ResponseEntity<?> twitchCallback(@RequestParam String code, @RequestParam String state) {

        String jwt = state;
        if (jwt == null || jwt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token de autenticação ausente");
        }

        String login;
        System.out.print("aaaaaaaaaaaaaaaaaa");
        System.out.print(jwt);
        try {
            login = tokenService.getLoginFromToken(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
        // 1. Trocar o code por um access_token
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://id.twitch.tv/oauth2/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao obter token da Twitch");
        }

        String accessToken = (String) response.getBody().get("access_token");

        // 2. Buscar dados do usuário da Twitch
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(accessToken);
        authHeaders.add("Client-Id", clientId);

        HttpEntity<Void> userRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<Map> userResponse = restTemplate.exchange(
                "https://api.twitch.tv/helix/users", HttpMethod.GET, userRequest, Map.class);

        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao buscar usuário da Twitch");
        }

        Map userData = (Map) ((List<?>) userResponse.getBody().get("data")).get(0);
        String twitchUserId = (String) userData.get("id");
        String twitchLogin = (String) userData.get("login");
        String twitchDisplayName = (String) userData.get("display_name");

        // 3. Vincular ao usuário logado com base no Principal (JWT)
        fanService.vincularContaTwitch(login, twitchUserId, twitchLogin, twitchDisplayName);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "http://localhost:5173/user") // Redireciona para /user
                .build();
    }
}

