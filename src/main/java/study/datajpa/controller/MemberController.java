package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    //아래 2개는 같은 결과를 반환한다(도메인 클래스 컨버터)
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername(); //조회 목적으로만 쓸 수 있다. 데이터 변경은 안됨
    }


    //httpbinding용 데이터 삽입
   /* @PostConstruct
    public void init(){
        memberRepository.save(new Member("userA"));

    }*/


    @GetMapping("/members") //하위는 webpage 전역설정(ymml파일)보다 우선권을 갖는다
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {//pageable이 존재하면 pagerequest를 생성해서 injection 해준다.
        //DTO로 변환
        return memberRepository.findAll(pageable)
               // .map(member -> new MemberDto(member.getId(), member.getUsername(), null)); //map을 통해 자동으로 객체 담음
                    .map(MemberDto::new); //MemberDto에서 member를 받아 매소드 지정한 경우 매소드 reference로 바꿔서 작성할 수 있다.
                        //클래스이름 :: 메소드이름
    }

    @PostConstruct
    public void init() {
        for (int i= 0; i <100 ; i++){
            memberRepository.save(new Member("user" +i, i));
        }
    }
}
