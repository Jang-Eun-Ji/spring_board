package com.encore.board.post.contorller;


import com.encore.board.post.dto.PostDetailResDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostReqUpdateDto;
import com.encore.board.post.dto.PostSaveReqDto;
import com.encore.board.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/create")
    public String postCreate(){
        return "post/post-create";
    }

    @PostMapping("/post/create")
    public String postWrite(Model model, PostSaveReqDto postSaveReqDto, HttpSession httpSession){
        try {
            // HttpServletRequest request를 매개변수에 주입한 뒤에
//            HttpSession session = request.getSession(); 세선걊을 꺼내어 getAttribute("email")
            postService.save(postSaveReqDto, httpSession.getAttribute("email").toString());
            return "redirect:/post/list";
        } catch(IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "post/post-create";
        }
    }

    @PostMapping("/post/{id}/update")
    public String postUpdate(@PathVariable Long id, PostReqUpdateDto postReqUpdateDto){
        postService.update(id, postReqUpdateDto);
        return "redirect:/post/detail/" + id;
    }

    @GetMapping("/post/list")
//    localhost:8080/post/list?size=xx&sort=xx,desc
    public String postList(Model model, @PageableDefault(size = 5, sort = "createdTime" , direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostListResDto> postListResDtos = postService.findByAppointment(pageable);
        model.addAttribute("postList", postListResDtos);
        return "post/post-list";
    }
//    @GetMapping("/json/post/list")
//    @ResponseBody
//    public Page<PostListResDto> postList(Pageable pageable){
//        return postService.findAllJson(pageable);
//    }

    @GetMapping("/post/detail/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        PostDetailResDto postDetailResDto = postService.findPostDetail(id);
        model.addAttribute("post", postDetailResDto);
        return "post/post-detail";
    }

    @GetMapping("/post/delete/{id}")
    public String postDelete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/post/list";
    }

}
