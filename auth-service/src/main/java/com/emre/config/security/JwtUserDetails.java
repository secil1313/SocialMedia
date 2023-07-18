package com.emre.config.security;

import com.emre.repository.entity.Auth;
import com.emre.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {
    private final AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Optional<Auth> auth = authService.findById(id);
        if (auth.isPresent()) {

            /**
             * Rolleri normalde liste şeklinde tutarız ama bu uygulamada tek bir rolü varmış gibi tuttuk.
             * Eğer liste olarak tutarsak -->
             * List<GrantedAuthority> authorityList = new ArrayList<>();
             * Auth authRole = authService.findById(id);
             * authRole.getRoles().foreach(roles ->
             *     {
             *          authorityList.add(new SimpleGrantedAuthority(roles));
             *     }
             *     );
             */
            //Bir yetkilendirme mekanizmasını ve kullanıcının rollerini temsil eder.
            //SimpleGrantedAuthority --> Kullanıcının tek bir rolü olduğunu varsayar.
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(auth.get().getRole().toString());
            return User.builder()
                    .username(auth.get().getUsername())
                    //Uygulamada şifre doğrulama işlemi yapılmayacağı için boş geçilir.
                    .password("")
                    .accountExpired(false)
                    .accountLocked(false)
                    .authorities(grantedAuthority)
                    .build();
        }
        return null;
    }
}
