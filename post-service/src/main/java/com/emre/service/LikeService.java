package com.emre.service;


import com.emre.manager.IUserProfileManager;
import com.emre.repository.ILikeRepository;
import com.emre.repository.entity.Like;
import com.emre.utility.JwtTokenProvider;
import com.emre.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService extends ServiceManager<Like, String> {
    private final ILikeRepository likeRepository;


    public LikeService(ILikeRepository likeRepository) {
        super(likeRepository);
        this.likeRepository = likeRepository;
    }

    public Optional<Like> findByUserIdAndPostId(String userId, String postId) {
        Optional<Like> like = likeRepository.findByUserIdAndPostId(userId, postId);
        return like;
    }
    public void deleteByUserIdAndPostId(String userId,String postId) {
        likeRepository.deleteByUserIdAndPostId(userId,postId);
    }

}
