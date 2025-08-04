package me.shinsunyoung.springbootdeveloper.sevice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springbootdeveloper.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;
    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request, String userName){
        return blogRepository.save(request.toEntity(userName));
    }
    // DB에 저장되어있는 모든 데이터 조회
    public List<Article> findAll(){
        return blogRepository.findAll();
    }
    // DB의 데이터 한 건 조회
    public Article findById(Long id){
        return blogRepository.findById(id)
                // 조회결과가 없을때 예외처리를 실행
                .orElseThrow(()->new IllegalArgumentException("not found" + id));
    }
    // 데이터 삭제
    public void delete(Long id){
        Article article = blogRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found" + id));
        // 작성자와 로그인한 유저 아이디가 같으면 삭제
        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }
    // 데이터 수정하기
    @Transactional
    public Article update(long id, UpdateArticleRequest request, String loginUserEmail){
        // id를 이용하여 데이터를 변수에 저장
        Article article = blogRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found" + id));
        // @Transactional 어노테이션이 있다면
        // title과 content를 변경하는 메서드를 실행하는 순간 바로 db에 적용됨
        // -> blogRepository.save()실행할 필요가 없음
        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());
        // @Transactional이 없다면 아래와 같이 save를 실행해야함
        //blogRepository.save(article);
        return article;
    }
    public static void authorizeArticleAuthor(Article article){
        // 현재 로그인된 계정의 이름을 변수에 저장
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        // 현재 로그인한 계정의 이름과 게시글 작성자의 이름이 같은지 확인
        if(!article.getAuthor().equals(userName)){
            throw new IllegalArgumentException("not authorize");
        }
    }
}
