package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * save할 떼 반환타입을 void로 해도 되지만
     * DB에 저장하는 중요하고, 부작용이 적어야하는 기능이기 때문에
     * 결과를 조회하기 위해 ID값을 반환하도록 한다.
     *
     * @param member
     * @return member.getId()
     */
    public Long save(Member member) {
        entityManager.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }
}
