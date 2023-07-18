package com.emre.service;

import com.emre.dto.request.CreateCommentDto;
import com.emre.dto.request.CreateNewPostRequestDto;
import com.emre.dto.request.UpdatePostRequestDto;
import com.emre.dto.response.UserProfileResponseDto;
import com.emre.exception.ErrorType;
import com.emre.exception.PostManagerException;
import com.emre.manager.IUserProfileManager;
import com.emre.mapper.IPostMapper;
import com.emre.rabbitmq.model.CreatePostModel;
import com.emre.rabbitmq.model.UserProfileResponseModel;
import com.emre.rabbitmq.producer.CreatePostProducer;
import com.emre.repository.IPostRepository;
import com.emre.repository.entity.Comment;
import com.emre.repository.entity.Dislike;
import com.emre.repository.entity.Like;
import com.emre.repository.entity.Post;
import com.emre.utility.JwtTokenProvider;
import com.emre.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService extends ServiceManager<Post, String> {
    private final IPostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserProfileManager userProfileManager;
    private final CreatePostProducer createPostProducer;
    private final LikeService likeService;
    private final DislikeService dislikeService;
    private final CommentService commentService;

    public PostService(IPostRepository postRepository, JwtTokenProvider jwtTokenProvider, IUserProfileManager userProfileManager, CreatePostProducer createPostProducer, LikeService likeService, DislikeService dislikeService, CommentService commentService) {
        super(postRepository);
        this.postRepository = postRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProfileManager = userProfileManager;
        this.createPostProducer = createPostProducer;
        this.likeService = likeService;
        this.dislikeService = dislikeService;
        this.commentService = commentService;
    }

    public Post createPost(String token, CreateNewPostRequestDto dto) {
        //Kullanıcı giriş yapmış mı diye kontrol
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) throw new PostManagerException(ErrorType.INVALID_TOKEN);

        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody(); //getBody -> ResponseEntity'den ayırmak için
        //Post post = IPostMapper.INSTANCE.toPost(dto);
        //post.setUserId(userProfile.getUserId());
        //post.setUsername(userProfile.getUsername());
        //post.setAvatar(userProfile.getAvatar());
        Post post = Post.builder().userId(userProfile.getId())
                .username(userProfile.getUsername())
                .avatar(userProfile.getAvatar())
                .content(dto.getContent())
                .mediaUrls(dto.getMediaUrls())
                .build();
        return save(post);
    }

    //rabbitmq lu create de yaz.
    public Post createPostWithRabbitMq(String token, CreateNewPostRequestDto dto) {
        //Kullanıcı giriş yapmış mı diye kontrol
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) throw new PostManagerException(ErrorType.INVALID_TOKEN);

        CreatePostModel model = CreatePostModel.builder().authId(authId.get()).build();

        //Burdan bir cevap döneceği için, bu cevabı alacağımız verilere cast edip eşitleyebiliriz.
        UserProfileResponseModel userProfile = (UserProfileResponseModel) createPostProducer.createPost(model);

        Post post = IPostMapper.INSTANCE.toPost(dto);
        post.setUserId(userProfile.getUserId());
        post.setUsername(userProfile.getUsername());
        post.setAvatar(userProfile.getAvatar());
        return save(post);
    }

    public Post update(String token, UpdatePostRequestDto dto) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) throw new PostManagerException(ErrorType.INVALID_TOKEN);
        //authId.orElseThrow(() -> new PostManagerException(ErrorType.INVALID_TOKEN)); //Optional veri için if gigi kontrol yöntemi
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
        Optional<Post> post = postRepository.findById(dto.getId());
        if (userProfile.getId().equals(post.get().getUserId())) {
            post.get().getMediaUrls().addAll(dto.getAddMediaUrls());
            post.get().getMediaUrls().removeAll(dto.getRemoveMediaUrls());
            post.get().setContent(dto.getContent());
            return update(post.get());
        }
        throw new PostManagerException(ErrorType.POST_NOT_FOUND);
    }

    public Boolean likePost(String token, String postId) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) throw new PostManagerException(ErrorType.INVALID_TOKEN);
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();

        Optional<Post> post = postRepository.findById(postId);

        Optional<Like> optionalLike = likeService.findByUserIdAndPostId(userProfile.getId(), postId);
        if (optionalLike.isPresent()) {
            post.get().getLikes().remove(userProfile.getId());
            update(post.get());
            likeService.delete(optionalLike.get());
            return true;
        } else {
            Like like = Like.builder()
                    .postId(postId)
                    .userId(userProfile.getId())
                    .username(userProfile.getUsername())
                    .avatar(userProfile.getAvatar())
                    .build();
            likeService.save(like);
            if (post.isEmpty()) throw new RuntimeException("Post Bulunamadı");
            post.get().getLikes().add(userProfile.getId());
            update(post.get());
            return true;
        }
    }
    public Boolean dislikePost(String token, String postId) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) throw new PostManagerException(ErrorType.INVALID_TOKEN);
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();

        Optional<Post> post = postRepository.findById(postId);

        Optional<Dislike> optionalDislike = dislikeService.findByUserIdAndPostId(userProfile.getId(), postId);
        if (optionalDislike.isPresent()) {
            post.get().getDislikes().remove(userProfile.getId());
            update(post.get());
            dislikeService.delete(optionalDislike.get());
            return true;
        } else {
            Dislike dislike = Dislike.builder()
                    .postId(postId)
                    .userId(userProfile.getId())
                    .username(userProfile.getUsername())
                    .avatar(userProfile.getAvatar())
                    .build();
            dislikeService.save(dislike);
            if (post.isEmpty()) throw new RuntimeException("Post Bulunamadı");
            post.get().getDislikes().add(userProfile.getId());
            update(post.get());
            return true;
        }
    }

    public Boolean deletePost(String postId,String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) throw new PostManagerException(ErrorType.INVALID_TOKEN);
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
        Optional<Post> post = findById(postId);
        if (!post.get().getUserId().equals(userProfile.getId())) throw new RuntimeException("BU post bu kullanıcıya ait değil");
        post.get().getLikes().forEach(x -> likeService.deleteByUserIdAndPostId(x,postId));
        post.get().getDislikes().forEach(x -> dislikeService.deleteByUserIdAndPostId(x,postId));
        deleteById(post.get().getId());
        return true;
    }

    public String doCommentToPost(String token, CreateCommentDto dto) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) throw new PostManagerException(ErrorType.INVALID_TOKEN);
        UserProfileResponseDto userProfile = userProfileManager.findByAuthId(authId.get()).getBody();
        Optional<Post> post = postRepository.findById(dto.getPostId());
        if (post.isEmpty()) throw new PostManagerException(ErrorType.POST_NOT_FOUND);
        Comment doComment = Comment.builder()
                .comment(dto.getComment())
                .postId(dto.getPostId())
                .userId(userProfile.getId())
                .username(userProfile.getUsername())
                .build();
        commentService.save(doComment);
        if (dto.getCommentId() != null) {
            Optional<Comment> optionalComment = commentService.findById(dto.getCommentId());
            if (optionalComment.isEmpty()) throw new RuntimeException("Böyle bir yorum bulunamadı");
            optionalComment.get().getSubComment().add(doComment.getId());
            commentService.update(optionalComment.get());
        }
        post.get().getComments().add(doComment.getId());
        update(post.get());
        return dto.getComment();
    }
}
