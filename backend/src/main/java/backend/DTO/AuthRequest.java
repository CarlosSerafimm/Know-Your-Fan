package backend.DTO;

import java.util.Date;

public record AuthRequest (String login,
                           String senha,
                           String nomeCompleto,
                           String endereco,
                           Date dataNascimento){
}
