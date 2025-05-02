package backend;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
		try{

		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
		tesseract.setLanguage("eng");

		String result = tesseract.doOCR(new File("D:\\Pichau\\Área de Trabalho\\programação\\Know-Your-Fan\\Know-Your-Fan\\backend\\src\\main\\resources\\image\\teste.png"));
			System.out.println("testeeeeeeeeeeeeeeeeeeeeeeee");
		System.out.println(result);
			System.out.println("testeeeeeeeeeeeeeeeeeeeeeeee");
		} catch ( TesseractException e){

		}

	}

}
