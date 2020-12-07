package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;


@RequiredArgsConstructor//구현할 인터페이스 이름  + Impl을 써주면 스프링이 알아서 상속함 ex)MemberRepositoryImpl은 MemberRepository 인터페이스 구현
public class MemberRepositoryImpl implements MemberRepositoryCustom{


    private final EntityManager em;

    /*
    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }*/

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();

    }
}
