package backend.DTO;

import backend.model.enums.*;

import java.time.LocalDate;
import java.util.List;

public record ResponseFanDTO(String login,
                             String nomeCompleto,
                             Estado estado,
                             LocalDate dataNascimento,
                             Genero genero,
                             List<Jogo> jogosFavoritos,
                             List<Evento> eventosParticipados,
                             List<Produto> produtosComprados,
                             List<Jogador> jogadoresFavoritos,
                             List<RedeSocial> redesSeguidas,
                             String twitter,
                             String instagram,
                             String linkedIn,
                             int pontuacao) {
}
