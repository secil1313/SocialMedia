package com.emre.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileChangePasswordResponseDto {
    //Unutulup, Değiştirilen şifreyi Auth'dan almak için kullanacağımız dto.
    Long authId;
    String password;
}
