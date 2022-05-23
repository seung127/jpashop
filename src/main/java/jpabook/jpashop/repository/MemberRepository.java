package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository //스프링 빈으로 등록해주는 에노테이션
@RequiredArgsConstructor //entitymanager도 이 에노테이션 사용 가능
public class MemberRepository {

    // @PersistenceContext jpa 만들어주는 에노테이션 | 알아서 주입해주는
    private final EntityManager em;



    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList(); //jpql문 | sql과의 차이는 이것은 엔티티자체의 쿼리를 날림
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name= :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }
}
