package study.datajpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository //외부 i/o 처리
public class MemberJpaRepository {

    @PersistenceContext //entitymanager 를 갖고온다
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;

    }


    public void delete(Member member) { //데이터 삭제
        em.remove(member);
    }


    public List<Member> findAll() { //객체를 대상(JPQL)
        return em.createQuery("select m from Member m", Member.class)
                .getResultList(); //리스트로 반환(여러건)
    }

    public Optional<Member> findById(Long id) { //optional로 조회
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult(); //결과를 하나만 반환(단건조회)
    }


    public Member find(Long id) { //find를 통해 해당 id를 가져온다
        return em.find(Member.class, id);
    }

    public List<Member> findByUserNameANdAgeGreaterThen(String username, int age) {
        return em.createQuery("select m from Member m where m.username =:username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();


    }

    public List<Member> findByPage(int age, int offset, int limit) { //page 조회
        return em.createQuery("select m from Member m where m.age =:age order by m.username desc")
                .setParameter("age", age) //전달할 변수
                .setFirstResult(offset) //어디서 시작해서 가져올건지
                .setMaxResults(limit) //몇개를 가져올건지
                .getResultList();
    }

    public long totalCount(int age){ //total count가져오기
        return em.createQuery("select count(m) from Member m where m.age =:age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age){ //벌크 업데이트
        int resultCount = em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                        .setParameter("age",age)
                        .executeUpdate();
        return resultCount; //인라인 ctirl + alt + n

    }



}

