package com.emre.repository;

import com.emre.repository.entity.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFollowRepository extends MongoRepository<Follow, String> {
    Optional<Follow> findByUserIdAndFollowId(String userId, String followId);
}
