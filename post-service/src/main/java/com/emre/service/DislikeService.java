package com.emre.service;

import com.emre.repository.IDislikeRepository;
import com.emre.repository.entity.Dislike;
import com.emre.repository.entity.Like;
import com.emre.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class DislikeService extends ServiceManager<Dislike,String> {
    private final IDislikeRepository dislikeRepository;


    public DislikeService(IDislikeRepository dislikeRepository) {
        super(dislikeRepository);
        this.dislikeRepository = dislikeRepository;
    }

    public Optional<Dislike> findByUserIdAndPostId(String userId, String postId) {
        Optional<Dislike> dislike = dislikeRepository.findByUserIdAndPostId(userId, postId);
        return dislike;
    }
    public void deleteByUserIdAndPostId(String userId,String postId) {
        dislikeRepository.deleteByUserIdAndPostId(userId,postId);
    }
}
