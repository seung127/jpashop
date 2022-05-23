package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.GeneratedValue;
import java.util.List;

@Service
@Transactional(readOnly = true) //javax가 있고 spring 이 있다   |  spring 쓰는 것을 권장(더 많은 기능)
//더 디테일하게 만들어준다 (읽기 전용)
@RequiredArgsConstructor //알아서 injection
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional //이것은 쓰기 전용이므로 TRUE하면 안된다
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    //중복 회원 검사
    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
