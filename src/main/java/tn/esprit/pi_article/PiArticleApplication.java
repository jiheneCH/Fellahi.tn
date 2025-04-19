package tn.esprit.pi_article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class PiArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PiArticleApplication.class, args);
    }

}
