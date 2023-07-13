package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id") // DB에 표현될 칼럼명
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // Order 클래스의 private Member member;에서 따온 것, JoinColum 방식(DB 칼럼명을 씀)과 헷갈리지 않도록 조심!
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}