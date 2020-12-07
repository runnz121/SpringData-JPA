package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
                                                                        //구현한 인터페이스를 여기에 적어준다.
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); //JPA 쿼리 메소드를 이용하여 스프링이 스스로 메소드 이름을 분석하여 jpql을 생성하고 실행

    @Query(name = "Member.findByUsername") //Member 엔티티에는 Namedquery로 명시해 주고 인터페이스에 이러한 문구를 적어주면 알아서 찾는다
    List<Member> findByUsername(@Param("username") String username);




    @Query("select m from Member m where m.username =:username and m.age =:age") //JPA Query를 바로 작성할 수있다. (위의 조회보다 단순) =:username은 꼭 붙여써야된다
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m") //username모두 조회
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t") //Dto로 조회시 select뒤에 new 패키지명 클래스까지(dto있는곳)적어줘야함
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")//컬렉션 파라미터 바인딩
    List<Member> findByNames(@Param("names") Collection<String> names);


    List<Member> findListByUsername(String username); //컬렉션조회
    Member findMemberByUsername(String username); //단건 조회
    Optional<Member> findOptionalByUsername(String username); //단건 Optional 조회


    //메서드 이름으로 쿼리만듬
    //page 조회시
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    //Slice 조회시
    //Slice<Member> findByAge(int age, Pageable pageable);


    @Modifying(clearAutomatically = true)//Jpa의 executeupdate()역할 안써주면 에러남 //clearAutomatically = true로 옵션 넣을시 매소드에서 entitymanager.clear() 넣지 않아도된다.
    @Query("update Member m set m.age = m.age + 1 where m.age >=:age")
    //반환타입 맞춰준다
   int bulkAgePlus(@Param("age") int age);


    @Query("select m from Member m left join fetch m.team")//fetch 써주면 맴버 조회할떄 팀 관련 값도 같이 조회해서 가져옴옴
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"}) //entitygraph를 통해 위의 구문의 결과를 그대로 도출
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")//fetch 써주면 맴버 조회할떄 팀 관련 값도 같이 조회해서 가져옴옴
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username")String username);



    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))//readonly는 변경감지 체크 안하고 바로 실행
    Member findReadOnlyByUsername(String username);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);


    List<UsernameOnly> findProjectionsByUsername (String username);


}


