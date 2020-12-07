package study.datajpa.repository;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext //영속성 컨테스트 이용
    EntityManager em;


    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Optional<Member> byId = memberRepository.findById(savedMember.getId());
        Member member1 = byId.get();

        assertThat(member1.getId()).isEqualTo(member.getId());
        assertThat(member1.getUsername()).isEqualTo(member.getUsername());
        assertThat(member1).isEqualTo(member);


    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15); //인터페이스 구현만 해놓은
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() { //MemberReposiotry interface에서 호출된 namedquery로 진행되는 테스트
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA"); //인터페이스 구현만 해놓은
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }


    @Test//인터페이스에서 쿼리를 바로 생성 및 구현
    public void testQuery() { //MemberReposiotry interface에서 호출된 namedquery로 진행되는 테스트
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);

    }

    @Test//유저 조회 테스트
    public void findUsernameList() { //MemberReposiotry interface에서 호출된 findUsernameListy로 진행되는 테스트
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) { //for : 구문 usernameList에서 객체를 꺼내 s에 넣겠다 https://m.blog.naver.com/kokolisy/110121503035
            System.out.println("s = " + s);
        }

    }

    @Test//DTo조회
    public void findMemberDto() { //MemberReposiotry interface에서 호출된 findUsernameListy로 진행되는 테스트
        Team team = new Team("teamA");
        teamRepository.save(team);


        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }

    }

    @Test//컬렉션 조회 테스트
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }

    }

    @Test//다양한 리턴타입 테스트
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        Member findMember = memberRepository.findMemberByUsername("AAA");//컬렉션으로 반환시 null 이여도 반환되기 떄문에 이럴 경우엔 if보다 List<xxx>로 받는게 좋다 ex)List<Member> member = /....
        System.out.println("findMember = " + findMember);

    }

    @Test//page 테스트
    public void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));


        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username")); //0페이지에서 3개 가져와

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);//상위로 올라가면 pageable도 속해있다.
        //반환타입을 page로 받으면 totalcount 쿼리도 같이 날린다

        //아래 람다식을 풀어서 쓰면 이와같다.
        /*List<MemberDto> lst = new ArrayList<>();//MemberDto 리스트 생성
        for(Member member : page) {             //page에서 객체 꺼내 member 객체로 넣는다.
            lst.add(new MemberDto(member.getId(), member.getUsername(),null)); //새로운 객체에 넣는 조건은 다음과같다
        }*/

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null)); //1.람다함수 메서드에서 이름과 반환타입 제거  2. 매개변수 선언부와 몸통{} 사이에 -> 추가


        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        for (Member member : content) {
            System.out.println("member = " + member);

        }
        System.out.println("totalElements = " + totalElements);


    }

   /* @Test//Slice 테스트 위 테스트와 비교해서 주석처리된건 slice엔 없는 기능들이다//slice는 요청갯수보다 +1개를 더 가져온다
    public void Slice(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));


        int age = 10;
        PageRequest pageRequest= PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username")); //0페이지에서 3개 가져와

        //when
        Slice<Member> page = memberRepository.findByAge(age, pageRequest);//상위로 올라가면 pageable도 속해있다.

        //then
        List<Member> content = page.getContent();
        // long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
       // assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
       // assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        for (Member member : content) {
            System.out.println("member = " + member);

        }
       // System.out.println("totalElements = " + totalElements);


    }*/

    @Test
    public void bulkUpdate() { //벌크 업데이트 테스트
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 16));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        //벌크 업데이트의 경우 flushk 와 clear로 모두 날려줘야 한다.
        //em.flush();
        //em.clear();//영속성 context 모두 날려버린다(@Modify의 clearAutomatically = true옵션 넣으면 없어도된다)


        List<Member> result = memberRepository.findListByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        //then
        assertThat(resultCount).isEqualTo(3);


    }

    @Test
    public void findMemberLazy(){ //fetch join 테스트
        //given
        //member1 -> teamA를 참조
        //member2 -> teamB참조

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        
        em.flush();
        em.clear();
        
        //when
        //select Member 쿼리만 진행
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam() = " + member.getTeam().getName());
            System.out.println("member.class() = " + member.getTeam().getClass());
            //처음에 getusername으로 쿼리한번 나가고(이때 team의 객체는 가짜 객체를 반환한다.fetch가 lazy 로 되어있음Member class0
            //후에 team의 실제 이름을 가져오기 위해 쿼리가 한번더 수행된다(N+1번 쿼리 수행된다 즉 10번 결과가 나오면 10번만큼 더 쿼리를 수행)

        }
        
    }

    @Test
    public void queryHint(){
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2"); //위에 저장한 findmember의 member1을 member2 로 변경

        em.flush();
        //then


    }

    @Test
    public void lock(){ //lockmode test

        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");


        em.flush();

    }



}
