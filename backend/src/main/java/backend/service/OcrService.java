package backend.service;

import net.sourceforge.tess4j.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class OcrService {

    public String extrairTexto(MultipartFile file) throws IOException {
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

}

