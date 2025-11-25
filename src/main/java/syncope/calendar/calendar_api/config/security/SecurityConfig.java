package syncope.calendar.calendar_api.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()  // <-- Muito importante
                        .anyRequest().permitAll()
                )
                .securityMatcher("/**")  // <-- força matcher direto
                .httpBasic(httpBasic -> httpBasic.disable()) // <-- desabilita basic auth
                .formLogin(form -> form.disable()) // <-- desabilita form login
                .logout(logout -> logout.disable()); // <-- desabilita logout

        return http.build();
    }
}
