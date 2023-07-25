package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jpabook.jpashop.domain.OrderStatus.ORDER;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 연관관계의 주인으로 본다
    @ManyToOne(fetch = FetchType.LAZY) // 모든 연관관계는 지연로딩으로 설정
    @JoinColumn(name = "member_id") // 어떤 칼럼을 조인칼럼으로 쓸것인가? ==> Member의 member_id를 조인칼럼으로 쓰겠다(DB 관점)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>(); // 컬렉션은 필드에서 초기화하자

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 모든 연관관계는 지연로딩으로 설정한다
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 여기서는 테이블 access를 Order에 많이 하므로 1대1 연관관계의 주인을 Order로 했지만 Delivery에 놔도 된다(사람에 따라 다른 것, 답은 없다)

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태 [ORDER, CANCLE]

    /* 연관관계 편의 메소드 생성, 양방향 관계에서 주인쪽(FK소유)에 메소드를 만들어주는게 좋다*/
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /* 생성 메서드, OrderItem...은 여러개를 의미한다 */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
}