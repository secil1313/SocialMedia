package com.emre.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//hasRole kullanabilmek için aslında. Rol bazlı kontrol edilecekse ve bu metodda yazılacaksa, kullanılması gerekiyor.
@EnableGlobalMethodSecurity(prePostEnabled = true) //metodları işleyerek rol kontrolü yapmayı sağlar.
public class SecurityConfig {
    @Bean
    JwtFilter getJwtFilter() {
        return new JwtFilter();
    }

    /**
     * SecurityFilterChain -> Gelen istekleri işlemek ve filtreleyerek bir zincir oluşturmayı sağlar
     * @param httpSecurity -> Http işlemlerinde ki güvenlikten sorumludur. Kimlik doğrulama veya session işlemlerinde
     *                     hangi kişinin endpointe erişip erişemeceğini belirler. Rollere göre kontrol eder.
     *                     cors -> http'nin oluşturabileceği güvenlik açıklarını gidermek için browser'ların kullandığı bir erişim kısıtlama prensibi.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**").permitAll().anyRequest().authenticated();
        httpSecurity.addFilterBefore(getJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
