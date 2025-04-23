package tn.esprit.fallehiuser.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Applique à toutes les routes
                .allowedOrigins("http://localhost:4200")  // L'origine de votre application Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Méthodes HTTP autorisées
                .allowedHeaders("*")  // Autorise tous les en-têtes
                .allowCredentials(true);  // Si vous avez besoin des cookies/authentification
    }

    // Exposer les fichiers locaux via URL
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**") // URL visible dans le navigateur
                .addResourceLocations("file:///C:/uploads/"); // Emplacement local sur ton PC
    }


}