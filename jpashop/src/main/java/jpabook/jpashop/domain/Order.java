package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 연관관계의 주인으로 본다
    @ManyToOne
    @JoinColumn(name = "member_id") // 어떤 칼럼을 조인칼럼으로 쓸것인가? ==> Member의 member_id를 조인칼럼으로 쓰겠다(DB 관점)
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 여기서는 테이블 access를 Order에 많이 하므로 1대1 연관관계의 주인을 Order로 했지만 Delivery에 놔도 된다(사람에 따라 다른 것, 답은 없다)

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태 [ORDER, CANCLE]
}