package backend.controller;

import backend.model.enums.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/enums")
public class EnumsController {

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getAll() {
        Map<String, List<String>> enumsMap = new HashMap<>();

        enumsMap.put("Genero", Arrays.stream(Genero.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        enumsMap.put("Estado", Arrays.stream(Estado.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        enumsMap.put("Evento", Arrays.stream(Evento.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        enumsMap.put("Jogador", Arrays.stream(Jogador.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        enumsMap.put("Jogo", Arrays.stream(Jogo.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        enumsMap.put("Plataforma", Arrays.stream(Plataforma.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        enumsMap.put("Produto", Arrays.stream(Produto.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        enumsMap.put("RedeSocial", Arrays.stream(RedeSocial.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(enumsMap);
    }
}
