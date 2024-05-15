package una.ac.cr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import una.ac.cr.logic.Usuarios;

@SpringBootApplication
public class ProyectoFacturacionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProyectoFacturacionApplication.class, args);
        Usuarios usuario = Usuarios.builder()
                .identificacion("")
                .contrasena("")
                .rol("")
                .build();

    }

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var chain = http
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/api/usuarios").permitAll()
                        .requestMatchers("/api/login/login").permitAll()
                        .requestMatchers("/api/login/logout").authenticated()
                        .requestMatchers(HttpMethod.POST, "/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/api/**").hasAnyAuthority("ADMIN", "PROVEE")
                        .requestMatchers("/**").permitAll()
                )
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .csrf().disable()
                .build();
        return chain;
    }*/


    @Bean("securityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var chain = http
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/api/usuarios").permitAll()
                        .requestMatchers("/api/login/login").permitAll()
                        .requestMatchers("/api/usuarios").permitAll()
                        .requestMatchers("/api/login/logout").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/api/**").hasAnyAuthority("ADMIN","PROVEE")
                        .requestMatchers("/**").permitAll()
                )
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .csrf().disable()
                .build();
        return chain;
    }
}