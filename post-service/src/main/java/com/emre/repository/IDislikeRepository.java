package com.emre.repository;

import com.emre.repository.entity.Dislike;
import com.emre.repository.entity.Like;
import com.emre.repository.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDislikeRepository extends MongoRepository<Dislike,String> {
    Optional<Dislike> findByUserIdAndPostId(String userId, String postId);

    void deleteByUserIdAndPostId(String userId,String postId);
}
