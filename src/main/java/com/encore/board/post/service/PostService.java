package com.encore.board.post.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.PostDetailResDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostReqUpdateDto;
import com.encore.board.post.dto.PostSaveReqDto;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service //객체로 완성시켜 싱글톤 객체로 만드는것
@Transactional

public class PostService {
    private final PostRepository postRepository;
//    authorService에 findById를 만들어서 참조하면 나중에 authorService에서 postService가 필요할때 순환참조 에러가 날수도 있음
    private final AuthorRepository authorRepository;
    @Autowired // 생성자 하나면 자동으로 오토와이어 기능이 붙는디 그냥 써봄
    public PostService(PostRepository postRepository, AuthorRepository authorRepository){
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }


    public void save(PostSaveReqDto postSaveReqDto) throws IllegalArgumentException{
        Author author = authorRepository.findByEmail(postSaveReqDto.getEmail()).orElse(null);
        LocalDateTime localDateTime = null;
        String appointment = null;
        if (postSaveReqDto.getAppointment().equals("Y") &&
                !postSaveReqDto.getAppointmentTime().isEmpty()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            localDateTime = LocalDateTime.parse(postSaveReqDto.getAppointmentTime() , dateTimeFormatter);
            LocalDateTime now = LocalDateTime.now();
            if(localDateTime.isBefore(now)){
                throw new IllegalArgumentException("시간정보 잘못입력");
            }
            appointment = "Y";
        }
        Post post = Post.builder()
                .title(postSaveReqDto.getTitle())
                .contents(postSaveReqDto.getContents())
                .author(author)
                .appointMent(appointment)
                .appointMentTime(localDateTime)
                .build();
//        더티체킹 테스트
//        author.updateAuthor("dirty checking test", "1234");
//        authorRepository.save(author);

        postRepository.save(post);

    }
//    public List<PostListResDto> findAll(Pageable pageable){
//        List<Post> posts = postRepository.findAllFetchJoin(); // spring JPA 에서 제공하는 findAll이다
////        List<Post> postSaveReqDto = postRepository.findAllByOrderByCreatedTimeDesc();
//        List<PostListResDto> postLists = new ArrayList<>();
//        for(Post post: posts){
//            PostListResDto postListResDto = new PostListResDto();
//            postListResDto.setId(post.getId());
//            postListResDto.setTitle(post.getTitle());
//            postListResDto.setAuthor_email(post.getAuthor()==null?"익명":post.getAuthor().getEmail());
//            postLists.add(postListResDto);
//        }
//        return postLists;
//    }
    public Page<PostListResDto> findByAppointment(Pageable pageable){
        Page<Post> posts = postRepository.findByAppointMent(null, pageable);
        return posts.map(p-> new PostListResDto(p.getId(), p.getTitle(), p.getAuthor()==null?"익명유저":p.getAuthor().getEmail()));
    }
    public Page<PostListResDto> findAllPaging(Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(p-> new PostListResDto(p.getId(), p.getTitle(), p.getAuthor()==null?"익명유저":p.getAuthor().getEmail()));
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
//        명시적으로 save를 하지 않더라도, jpa의 영속성컨텍스트를 통해, 객체에 변경이 감지(dirtychecking)되면, 트랜잭션이 완료되는 시점에 save동작
//        postRepository.save(post);
    }

    public void delete(Long id) throws EntityNotFoundException{
        postRepository.deleteById(id);
    }

}
