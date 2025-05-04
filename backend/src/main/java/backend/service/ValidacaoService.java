package backend.service;

import backend.model.Fan;
import backend.repository.FanRepository;
import net.sourceforge.tess4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Service
public class ValidacaoService {

    @Autowired
    private FanRepository fanRepository;

    private String extrairTexto(MultipartFile file) throws IOException {
        File tempFile = convertMultipartFileToFile(file);

        if (!tempFile.exists() || tempFile.length() == 0) {
            throw new RuntimeException("Arquivo temporário está vazio ou não foi criado corretamente.");
        }

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata"); // ou onde estiver o tessdata
        tesseract.setLanguage("eng");

        try {
            tesseract.setLanguage("eng");
            return tesseract.doOCR(tempFile);
        } catch (TesseractException e) {
            throw new RuntimeException("Erro ao realizar OCR: " + e.getMessage());
        } finally {
            tempFile.delete();
        }
    }


    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());

        file.transferTo(tempFile);

        System.out.println("Arquivo salvo em: " + tempFile.getAbsolutePath());

        return tempFile;
    }

    public String validar(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Fan Teste = (Fan) authentication.getPrincipal();
        Fan fan = fanRepository.findByLogin(Teste.getLogin());

        if (fan == null) new RuntimeException("Usuário não encontrado");

        try {
            if (fan.getNomeCompleto() == null || fan.getNomeCompleto().trim() == "") {

                return "Antes de validar insira um nome completo";
            }
            String nomeCompleto = fan.getNomeCompleto();
            String[] partesNome = nomeCompleto.split("\\s+");

            String textoExtraido = extrairTexto(file);
            String[] partesTexto = textoExtraido.split("\\s+");

            boolean correspondencia = Arrays.stream(partesNome)
                    .anyMatch(nome -> Arrays.stream(partesTexto)
                            .anyMatch(texto -> texto.equalsIgnoreCase(nome)));

            if (correspondencia) {
                fan.setValidado(true);
                fanRepository.save(fan);
                return "Documento processado e fan validado com sucesso.";
            } else {
                return "Documento processado, mas nenhuma correspondência foi encontrada.";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

