package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception{
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/");

        http
                .authorizeHttpRequests((auth) ->auth
                        .requestMatchers(mvcMatcherBuilder.pattern("/design"))
                        .permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/orders"))
                        .hasRole("USER")
                        .requestMatchers(mvcMatcherBuilder.pattern("/")
                                , mvcMatcherBuilder.pattern("/**"))
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        UserDetails user1 = User.builder()
                .username("user1")
                .password("{noop}password1")
                .authorities("USER")
                .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password("{noop}password2")
                .authorities("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
}
