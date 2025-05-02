package backend.controller;

import backend.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @PostMapping("/upload")
    public String uploadDocumento(@RequestParam("file") MultipartFile file) {
        try {
            return ocrService.extrairTexto(file);
        } catch (Exception e) {
            return "Erro ao processar o documento: " + e.getMessage();
        }
    }
}

