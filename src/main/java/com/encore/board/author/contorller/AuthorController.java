package com.encore.board.author.contorller;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorReqUpdateDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthorController {
    private final AuthorService authorService;
    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/author/create")
    public String authorCreate(){
        return "/author/author-create";
    }

    @PostMapping("/author/create")
    public String authorSave(Model model, AuthorSaveReqDto authorSaveReqDto){
        try {
            authorService.save(authorSaveReqDto);
            return "redirect:/author/list";
        }catch (IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/author/create";
        }
    }
//    @PostMapping("author/create")
//    public String authorSave(AuthorSaveReqDto authorSaveReqDto, RedirectAttributes redirectAttributes){
//        try {
//            authorService.save(authorSaveReqDto);
//            return "redirect:/author/list";
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//            redirectAttributes.addFlashAttribute("error", e.getMessage());
//            return "redirect:/author/create"; // 오류 시 양식 페이지로 리디렉션
//        }
//    }

    @GetMapping("/author/list")
    public String authorList(Model model){
        model.addAttribute("authorList", authorService.findAll());
        return "author/author-list";

    }

    @GetMapping("/author/detail/{id}")
    public String authorDetail(@PathVariable Long id, Model model ){
        model.addAttribute("authorDetail", authorService.findAuthorDetail(id));
        return "author/author-detail";
    }

    @PostMapping("/author/{id}/update")
    public String author(@PathVariable Long id, AuthorReqUpdateDto authorReqUpdateDto){
        authorService.update(id, authorReqUpdateDto);
        return "redirect:/author/detail/" + id;
    }
    @GetMapping("author/delete/{id}")
    public String authorDelete(@PathVariable Long id){
        authorService.delete(id);
        return "redirect:/author/list";
    }

//  엔티티 순환참조 이슈 테스트
    @GetMapping("/author/{id}/circle/entity") // 글쓴 사람은 post에서도 데이터를 가져와야해서 순환참조가 걸림/ 글안쓴사람은 post 데이터 안가져와서 순환참조 안걸림
    @ResponseBody
//    연관관계가 있는 author 엔티티를 json으로 직렬화를 하게 될 경우
//    순환참조 이슈 발생하므로, dto 사용 필요.
    public Author circleIssueTest1(@PathVariable Long id){
        return authorService.findById(id);
    }
    @GetMapping("/author/{id}/circle/dto") //dto에서 가져와서 순환참조 안걸림
    @ResponseBody
    public AuthorDetailResDto circlIssueTest2(@PathVariable Long id){
        return authorService.findAuthorDetail(id);
    }
}
