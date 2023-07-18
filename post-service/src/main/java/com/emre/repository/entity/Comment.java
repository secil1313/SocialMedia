package com.emre.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Comment extends Base{
    @Id
    private String id;
    private String userId; //Yorum yapanın id'si
    private String username; //Yorum yapanın kullanıcı adı
    private String postId;
    private String comment; //Yapılan yorum
    private List<String> subComment = new ArrayList<>(); //Bir yoruma, yorum yapanların yorum id'si (userId değil!)
    private List<String> commentLikes; //Yorumu beğenenlerin userId'leri
    private List<String> commentDislikes; //Yorumu beğenmeyenlerin userId'leri
}
