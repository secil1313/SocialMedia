package com.emre.repository.entity;

import com.emre.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserProfile extends Base implements Serializable { //Serializable, redis cache'de istediği için yazıldı.
    @Id
    private String id;
    private Long authId;
    private String username;
    private String email;
    private String password; //Daha sonradan eklendi, şifreyi güncelleyebilmek için ekledik aslında.
    private String phone;
    private String avatar;
    private String info;
    private String address;
    @Builder.Default
    private EStatus status = EStatus.PENDING;
    //follow, follower
    private List<String> follows = new ArrayList<>(); //işlem esnasında null hatası veriyor. o yüzden ilk olarak burda oluşturuyoruz.
    private List<String> followers = new ArrayList<>();
}
