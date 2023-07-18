package com.emre.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFollowDto {
    String token; //Giriş yapmış olan kullanıcı olacağı için, bunun da bir token ı olacak.
    // Yani önce adamın giriş yapmış olması gerekiyor ki takip edebilsin. Bu yüzden token ekledik.
    String followId;
}
