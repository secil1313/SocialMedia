package com.emre.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Like extends Base{
    @Id
    private String id;
    private String userId; //Beğenenin id'si
    private String postId; //beğendiği post'un id'si
    private String username; //Beğenenin adı
    private String avatar; //Beğenenin avatarı
}
