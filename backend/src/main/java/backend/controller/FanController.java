package backend.controller;

import backend.DTO.RequestFanDTO;
import backend.DTO.ResponseFanDTO;
import backend.model.Fan;
import backend.model.enums.Jogo;
import backend.model.mapper.FanMapper;
import backend.service.FanService;
import backend.service.ValidacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fan")
public class FanController {

    @Autowired
    private FanService fanService;
    @Autowired
    private FanMapper fanMapper;
    @Autowired
    private ValidacaoService validacaoService;

    @GetMapping("/pesquisar")
    public ResponseEntity<ResponseFanDTO> getFan() {



        ResponseFanDTO fan = fanService.getFan();

        return ResponseEntity.ok(fan);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ResponseFanDTO>> getAll() {
        List<Fan> all = fanService.getAll();
        List<ResponseFanDTO> lista = all.stream().map(fanMapper::entityToResponseFan).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping
    public ResponseEntity<ResponseFanDTO> updateFan(@RequestBody RequestFanDTO dto) {
        ResponseFanDTO updatedFan = fanService.updateLoggedFan(dto);
        return ResponseEntity.ok(updatedFan);
    }

    @PostMapping("/validar")
    public ResponseEntity<String> uploadDocumento(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(validacaoService.validar(file));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar o documento: " + e.getMessage());
        }
    }
}
