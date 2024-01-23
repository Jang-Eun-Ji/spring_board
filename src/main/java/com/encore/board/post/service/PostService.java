package com.encore.board.post.service;

import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.PostDetailResDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostReqUpdateDto;
import com.encore.board.post.dto.PostSaveReqDto;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional

public class PostService {
    private final PostRepository postRepository;
    @Autowired // 생성자 하나면 자동으로 오토와이어 기능이 붙는디 그냥 써봄
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public void save(PostSaveReqDto postSaveReqDto){
        Post post = new Post(postSaveReqDto.getTitle(), postSaveReqDto.getContents());
        postRepository.save(post);
    }
    public List<PostListResDto> findAll(){
        List<Post> posts = postRepository.findAll(); // spring JPA 에서 제공하는 findAll이다
        List<PostListResDto> postLists = new ArrayList<>();
        for(Post post: posts){
            PostListResDto postListResDto = new PostListResDto();
            postListResDto.setId(post.getId());
            postListResDto.setTitle(post.getTitle());
            postListResDto.setContents(post.getContents());
            postLists.add(postListResDto);
        }
        return postLists;
    }
    public Post findById(Long id) throws EntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);//값이 없으면
        return post;
    }
    public PostDetailResDto findPostDetail(Long id) throws EntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);//값이 없으면
        PostDetailResDto postDetailResDto = new PostDetailResDto();
        postDetailResDto.setId(post.getId());
        postDetailResDto.setTitle(post.getTitle());
        postDetailResDto.setContents(post.getContents());
        return postDetailResDto;
    }
    public void update(Long id, PostReqUpdateDto postReqUpdateDto) throws EntityNotFoundException {
        Post post = this.findById(id);
        post.updatePost(postReqUpdateDto.getTitle(), postReqUpdateDto.getContents());
        postRepository.save(post);
    }

    public void delete(Long id) throws EntityNotFoundException{
        postRepository.deleteById(id);
    }

}
