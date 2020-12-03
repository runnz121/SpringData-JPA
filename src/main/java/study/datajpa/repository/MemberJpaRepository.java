package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository //외부 i/o 처리
public class MemberJpaRepository {

    @PersistenceContext //entitymanager 를 갖고온다
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;

    }


    public Member find(Long id) { //find를 통해 해당 id를 가져온다
        return em.find(Member.class, id);
    }
}
