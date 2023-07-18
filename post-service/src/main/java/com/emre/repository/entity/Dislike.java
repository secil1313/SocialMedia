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
public class Dislike extends Base{
    @Id
    private String id;
    private String userId; //Beğenmeyenin id'si
    private String postId; //paylaşılan post'un id'si
    private String username; //Beğenmeyenin adı
    private String avatar; //Beğenmeyenin avatarı
}
