package backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestDotEnv {



    @Test
    void testDotEnv() {
         Dotenv dotenv = Dotenv.configure().load();

        String clientId = dotenv.get("TWITCH_CLIENT_ID");
        String clientSecret = dotenv.get("TWITCH_CLIENT_SECRET");
        String redirectUri = dotenv.get("TWITCH_REDIRECT_URI");

        System.out.println("Client ID: " + clientId);
        System.out.println("Client Secret: " + clientSecret);
        System.out.println("Redirect URI: " + redirectUri);
    }
}
