package me.shinsunyoung.springbootdeveloper.controller;


import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.dto.ArticleResponse;
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;

import me.shinsunyoung.springbootdeveloper.sevice.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {
    private final BlogService blogService;
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(
            @RequestBody AddArticleRequest request
            ,Principal principal){
        // 데이터 추가 서비스 실행 후 DB저장한 Entity를 변수에 저장
        Article saveArticle = blogService.save(request, principal.getName());
        // 응답 코드는 201번(CREATED) 응답 데이터는 변수에 저장한 saveArticle을 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saveArticle);
    }
    @GetMapping("/api/articles")
    // ResponseEntity에 List를 저장하는것도 가능
    public ResponseEntity<List<ArticleResponse>> findAllArticles(){
        // 테이블의 모든 데이터를 저장하는 articles 생성
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
//                .map(entity -> new ArticleResponse(entity))
                .toList();
//         return ResponseEntity.status(HttpStatus.OK).body(articles)
        return ResponseEntity.ok().body(articles);
    }
    // @PathVariable("주소창의{}안에 있는 문자열")
    // 주소창에있는 데이터를 매개변수에 저장
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(
            @PathVariable("id") long id){
        Article article = blogService.findById(id);
        return ResponseEntity.ok().body(new ArticleResponse(article));
    }
    @DeleteMapping("/api/articles/{id}")
    // 제너릭에 Void를 설정하여 아무 데이터도 전달하지 않도록 설정
    public ResponseEntity<Void> deleteArticle(
            @PathVariable("id") long id){
        blogService.delete(id);
        // 정상처리의 경우 아무 데이터도 전달하지 않음
        return ResponseEntity.ok().build();
    }
    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(
            @PathVariable("id") long id
            , @RequestBody UpdateArticleRequest request
            , Principal principal){
        // 로그인한 계정의 아이디를 저장
        String loginUserEmail = principal.getName();
        Article updateArticle = blogService.update(id, request, loginUserEmail);
        return ResponseEntity.ok().body(updateArticle);
    }
}
