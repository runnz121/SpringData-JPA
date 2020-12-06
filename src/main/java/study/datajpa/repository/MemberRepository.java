package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); //JPA 쿼리 메소드를 이용하여 스프링이 스스로 메소드 이름을 분석하여 jpql을 생성하고 실행

    @Query(name = "Member.findByUsername") //Member 엔티티에는 Namedquery로 명시해 주고 인터페이스에 이러한 문구를 적어주면 알아서 찾는다
    List<Member> findByUsername(@Param("username") String username);




    @Query("select m from Member m where m.username =:username and m.age =:age") //JPA Query를 바로 작성할 수있다. (위의 조회보다 단순) =:username은 꼭 붙여써야된다
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

}
