package com.encore.board.post.repository;

import com.encore.board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    List<Post> findAllByOrderByCreatedTimeDesc();
//    Pageable객체: pageNumber(page), page마다개수(size), 정렬(sort = 10), 정렬(sort=createdTime,desc)
//    Page: List<Post> + 해당 page의 각종 정보
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByAppointMent(String appointment, Pageable pageable);
//  select p.* from post p left join author a on p.author_id = a.id; - 밑에를 mariDB문으로 바꾸면
//  아래 jpql의 join문은 author객체를 통해 post를 스크리닝하고 싶은 상황에 적함
    @Query("select p from Post p left join p.author") // jpql문
    List<Post> findAllJoin();
    //  select p.*, a.* from post p left join author a on p.author_id = a.id; == mariDB문
    @Query("select p from Post p left join fetch p.author") // jpql문
    List<Post> findAllFetchJoin();
}
