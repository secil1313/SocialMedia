package com.emre.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity
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
                        "/v3/api-docs/**",
                        "/api/v1/auth/login",
                        "/api/v1/auth/login/Md5",
                        "/api/v1/auth/register2",
                        "/api/v1/auth/registerMD5",
                        "/api/v1/auth/activate-status",
                        "/api/v1/auth/forgot-password").permitAll() //Bu endpoint lere herkes erişebilir.
                //.antMatchers("/api/v1/auth/update").authenticated() //giriş yapan kişi(rolü önemli değil) update yapabilir.
                //.anyRequest().hasRole("ADMIN"); //Diğer bütün işlemler ADMIN rolü yapabilir.
                .anyRequest().authenticated();
        //JwtFilter'dan gelen bilgilere göre burada endpointler için son olarak bir filtreleme daha yapılır.
        httpSecurity.addFilterBefore(getJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
