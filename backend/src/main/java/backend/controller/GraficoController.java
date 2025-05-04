package backend.controller;

import backend.model.Fan;
import backend.model.enums.*;
import backend.repository.FanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/graficos")
public class GraficoController {

    @Autowired
    private FanRepository fanRepository;

    // Mapeia todos os dados dos Enums e conta as ocorrências
    @GetMapping("/dadosAgregados")
    public ResponseEntity<Map<String, Object>> getDadosAgregados() {
        List<Fan> fans = fanRepository.findAll();

        // Mapeamento para contagem dos Enums
        Map<String, Integer> generoCount = new HashMap<>();
        Map<String, Integer> estadoCount = new HashMap<>();
        Map<String, Integer> jogoCount = new HashMap<>();
        Map<String, Integer> eventoCount = new HashMap<>();
        Map<String, Integer> plataformaCount = new HashMap<>();
        Map<String, Integer> redeSocialCount = new HashMap<>();
        Map<String, Integer> produtoCount = new HashMap<>();
        Map<String, Integer> jogadorCount = new HashMap<>();

        // Inicializando os mapas com todos os valores dos enums
        for (Genero genero : Genero.values()) {
            generoCount.put(genero.name(), 0);
        }
        for (Estado estado : Estado.values()) {
            estadoCount.put(estado.name(), 0);
        }
        for (Jogo jogo : Jogo.values()) {
            jogoCount.put(jogo.name(), 0);
        }
        for (Evento evento : Evento.values()) {
            eventoCount.put(evento.name(), 0);
        }
        for (Plataforma plataforma : Plataforma.values()) {
            plataformaCount.put(plataforma.name(), 0);
        }
        for (RedeSocial redeSocial : RedeSocial.values()) {
            redeSocialCount.put(redeSocial.name(), 0);
        }
        for (Produto produto : Produto.values()) {
            produtoCount.put(produto.name(), 0);
        }
        for (Jogador jogador : Jogador.values()) {
            jogadorCount.put(jogador.name(), 0);
        }

        int validadoTrueCount = 0;
        int validadoFalseCount = 0;
        int segueFuriaTrueCount = 0;
        int segueFuriaFalseCount = 0;

        // Contando as ocorrências para cada usuário
        for (Fan fan : fans) {
            // Verificação de null e contagem de Gênero
            if (fan.getGenero() != null) {
                generoCount.merge(fan.getGenero().name(), 1, Integer::sum);
            }

            // Verificação de null e contagem de Estado
            if (fan.getEstado() != null) {
                estadoCount.merge(fan.getEstado().name(), 1, Integer::sum);
            }

            // Contagem de Jogos Favoritos (verificando se a lista não é null)
            if (fan.getJogosFavoritos() != null) {
                for (Jogo jogo : fan.getJogosFavoritos()) {
                    if (jogo != null) {
                        jogoCount.merge(jogo.name(), 1, Integer::sum);
                    }
                }
            }

            // Contagem de Eventos Participados (verificando se a lista não é null)
            if (fan.getEventosParticipados() != null) {
                for (Evento evento : fan.getEventosParticipados()) {
                    if (evento != null) {
                        eventoCount.merge(evento.name(), 1, Integer::sum);
                    }
                }
            }

            // Contagem de Plataformas Assistidas (verificando se a lista não é null)
            if (fan.getPlataformasAssistidas() != null) {
                for (Plataforma plataforma : fan.getPlataformasAssistidas()) {
                    if (plataforma != null) {
                        plataformaCount.merge(plataforma.name(), 1, Integer::sum);
                    }
                }
            }

            // Contagem de Redes Sociais Seguidas (verificando se a lista não é null)
            if (fan.getRedesSeguidas() != null) {
                for (RedeSocial redeSocial : fan.getRedesSeguidas()) {
                    if (redeSocial != null) {
                        redeSocialCount.merge(redeSocial.name(), 1, Integer::sum);
                    }
                }
            }
            if (fan.getProdutosComprados() != null) {
                for (Produto produto : fan.getProdutosComprados()) {
                    if (produto != null) {
                        produtoCount.merge(produto.name(), 1, Integer::sum);
                    }
                }
            }

            // Contagem de Jogadores Favoritos (verificando se a lista não é null)
            if (fan.getJogadoresFavoritos() != null) {
                for (Jogador jogador : fan.getJogadoresFavoritos()) {
                    if (jogador != null) {
                        jogadorCount.merge(jogador.name(), 1, Integer::sum);
                    }
                }
            }
            // Contagem de Validação e Se segue FURIA
            // Contagem de Validação
            if (fan.isValidado()) {
                validadoTrueCount++;
            } else {
                validadoFalseCount++;
            }

// Contagem de Se segue FURIA
            if (fan.isSegueFuria()) {
                segueFuriaTrueCount++;
            } else {
                segueFuriaFalseCount++;
            }

        }

        // Criando o objeto de resposta
        Map<String, Object> dadosAgregados = new HashMap<>();
        dadosAgregados.put("generoCount", generoCount);
        dadosAgregados.put("estadoCount", estadoCount);
        dadosAgregados.put("jogoCount", jogoCount);
        dadosAgregados.put("eventoCount", eventoCount);
        dadosAgregados.put("plataformaCount", plataformaCount);
        dadosAgregados.put("redeSocialCount", redeSocialCount);
        dadosAgregados.put("produtoCount", produtoCount);
        dadosAgregados.put("jogadorCount", jogadorCount);
        dadosAgregados.put("validadoTrueCount", validadoTrueCount);
        dadosAgregados.put("validadoFalseCount", validadoFalseCount);
        dadosAgregados.put("segueFuriaTrueCount", segueFuriaTrueCount);
        dadosAgregados.put("segueFuriaFalseCount", segueFuriaFalseCount);


        // Retornando os dados agregados
        return ResponseEntity.ok(dadosAgregados);
    }
}

