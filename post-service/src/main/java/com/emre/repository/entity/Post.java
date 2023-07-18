package com.emre.repository.entity;

import lombok.AllArgsConstructor;

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
public class Post extends Base {
    @Id
    private String id;
    private String userId; //Post'u paylaşan'ın id'si -> Bertan paylaştı
    private String username;
    private String avatar;
    private String content; //Post'un altında ki mesaj (yorum değil!)
    private List<String> mediaUrls = new ArrayList<>();
    private List<String> likes = new ArrayList<>(); //Post'u beğenen kişinin id'si -> Ünzile,Mücahit beğendi.
    private List<String> dislikes = new ArrayList<>();
    private List<String> comments = new ArrayList<>(); //Yorum yapanların id'leri
}
