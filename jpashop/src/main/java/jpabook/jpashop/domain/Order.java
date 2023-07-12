package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 연관관계의 주인으로 본다 ==> 아직 진행중
    @ManyToOne
    @JoinColumn(name = "member_id") // 어떤 칼럼을 조인칼럼으로 쓸것인가? ==> Member의 member_id를 조인칼럼으로 쓰겠다(DB 관점)
    private Member member;
}
