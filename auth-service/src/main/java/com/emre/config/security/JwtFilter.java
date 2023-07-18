package com.emre.config.security;

import com.emre.exception.AuthManagerException;
import com.emre.exception.ErrorType;
import com.emre.utility.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * OncePerRequestFilter -> Her bir istek iiçin bir kere çalışarak bir b-filtreleme işlemi sağlıyor.(Spring Framework sınıfıdır)
 */
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtUserDetails jwtUserDetails;

    /**
     * HttpServlet(senkron) --> Webflax(asenkron), OpenFeing, HttpClient, RestTemplate
     * HttpServlet, Java Servlet API tarafından sağlanan bir sınıftır ve HTTP protokolü üzerinden gelen istekleri işlemek için kullanılır.
     * Servlet, dinamik web uygulamaları geliştirmek için kullanılır ve doğrudan "web sunucusu"nda çalışır.
     * HTTP istekleri(GET,POST,DELETE,UPDATE,OPTIONS vb..)
     * <p>
     * HttpServletRequest --> Bir HTTP isteğinin sorgusunu ilgili sunucuya gönderie. İstemci tarafından sunucuya gönderilen isteği içerir.
     * Örn: istek başlıkları, parametreler,request yolu, HTTP yöntemi
     * HttpServletResponse --> Sunucudan geleen cevabı yakalıyor.
     * <p>
     * FilterChain --> İsteği işlemek ve yanıt üretmek için bir dizi filtreyi içeren yapıdır.
     * Bir HTTP isteği ServletRequest'e geldiğinde burada bir filtreleme ypılıyor ve sunucuya bu şekilde gönderilir.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeaderToken = request.getHeader("Authorization");
        System.out.println("Header'dan gelen token: " + authHeaderToken);
        if (authHeaderToken != null && authHeaderToken.startsWith("Bearer ")) {
            String token = authHeaderToken.substring(7);
            Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
            if (authId.isPresent()) {
                //Kullanıcının göndermiş olduğu token bilgisinin relo ve kimlik bilgilerinin doğrulanması
                UserDetails userDetails = jwtUserDetails.loadUserById(authId.get());
                //Kimlik doğrulama token'ını(authenticationToken) oluşturur
                // Bu token, kimlik doğrulama işlemlerinde kullanılır.
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //Kimlik doğrulamasının gerçekleştirildiğini  ve kimlik bilgilerinin doğru olduğu belirtiliyor
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else
                throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
        // Filtre zincirindeki bir sonraki filtreye
        filterChain.doFilter(request, response);
    }
}
