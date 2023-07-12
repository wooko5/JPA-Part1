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

    @OneToMany
    private List<Order> orders = new ArrayList<>();
}
