package backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

    @Bean
    public Dotenv dotenv() {
        Dotenv dotenv = Dotenv.configure().load();

        System.setProperty("TWITCH_CLIENT_ID", dotenv.get("TWITCH_CLIENT_ID"));
        System.setProperty("TWITCH_CLIENT_SECRET", dotenv.get("TWITCH_CLIENT_SECRET"));
        System.setProperty("TWITCH_REDIRECT_URI", dotenv.get("TWITCH_REDIRECT_URI"));

        return dotenv;
    }
}
