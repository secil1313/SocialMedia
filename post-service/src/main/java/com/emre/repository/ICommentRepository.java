package com.emre.repository;

import com.emre.repository.entity.Comment;
import com.emre.repository.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICommentRepository extends MongoRepository<Comment,String> {
}
