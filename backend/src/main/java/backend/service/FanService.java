package backend.service;

import backend.DTO.RequestFanDTO;
import backend.DTO.ResponseFanDTO;
import backend.model.Fan;
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

    public ResponseFanDTO getFan(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String login = (String) authentication.getPrincipal();
        Fan fan = fanRepository.findByLogin(login);
        return fanMapper.entityToResponseFan(fan);
    }

    public List<Fan> getAll(){
        return fanRepository.findAll();
    }
    public ResponseFanDTO updateLoggedFan(RequestFanDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = (String) authentication.getPrincipal();

        Fan fan = fanRepository.findByLogin(login);
        if (fan == null)  new RuntimeException("Usuário não encontrado");


        fanMapper.updateEntityFromDto(dto, fan);


        fanRepository.save(fan);
        return fanMapper.entityToResponseFan(fan);
    }
}
