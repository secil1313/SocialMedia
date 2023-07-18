package com.emre.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileChangePassword {
    //Unutulup, Değiştirilen şifreyi User'a aktarmak için kullanacağımız dto.
    Long authId;
    String password;
}
