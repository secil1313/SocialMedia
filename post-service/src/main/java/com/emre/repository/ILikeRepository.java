package com.emre.repository;

import com.emre.repository.entity.Like;
import com.emre.repository.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILikeRepository extends MongoRepository<Like,String> {
    Optional<Like> findByUserIdAndPostId(String userId,String postId);

    void deleteByUserIdAndPostId(String userId,String postId);
}
