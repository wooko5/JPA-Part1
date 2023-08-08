package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Order order) {
        entityManager.persist(order);
    }

    public Order findOne(Long id) {
        return entityManager.find(Order.class, id);
    }

    public void saveAll(List<Order> orderList) {
        for (Order order : orderList) {
            entityManager.persist(order);
        }
    }

    /* N+1 테스트용으로 잠시 해둠 */
    public List<Order> findAll() {
        return entityManager.createQuery("SELECT m FROM Order m").getResultList();
    }
}
