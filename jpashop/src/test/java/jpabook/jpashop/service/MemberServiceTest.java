package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager entityManager; // @Rollback(false)을 안 쓰고싶다면 'entityManager.flush()'을 사용하자

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("Jaeuk");

        /**
         * 실행 로그를 보면 저장 테스트인데 DB에 INSERT문을 실행하지 않는다.
         * 왜냐하면 JPA에서 em.persist(member)를 실행해도 DB에 INSERT문을 실행하는게 아니기 때문이다
         * JPA는 영속성 컨텍스트에 저장된 객체 데이터가 commit, flush가 실행되면 INSERT문을 생성하면서 DB에 전달되는 구조이기 때문이다
         *
         * commit, flush가 실행 안 되는 이유: @Transactional가 rollback이 기본적으로 true이기 때문이다
         * 그래서 해당 메소드에 @Rollback(false)로 두면 insert문이 실행된다
         */
        Long savedId = memberService.join(member); //when

        entityManager.flush();

        //then
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class) // try-catch문을 한 줄로 없앨 수 있다. 예외처리할 것을 expected에 넣기
    public void 중복_회원_예외() throws Exception {
        //given
        Member memberA = new Member();
        memberA.setName("Oh");

        Member memberB = new Member();
        memberB.setName("Oh");

        //when
        memberService.join(memberA);
        memberService.join(memberB);

//        try {
//            memberService.join(memberB);
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        fail("예외가 발생해야한다!!!");
    }


}