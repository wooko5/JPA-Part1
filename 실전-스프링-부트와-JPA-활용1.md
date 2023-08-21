## 실전-스프링-부트와-JPA-활용1



1. 프로젝트 환경설정

   - 프로젝트 생성

     - ```
       프로젝트 경로: C:\Users\USER\Desktop\Project\jpashop
       
       SpringBoot version: 2.7.13
       
       java version: 11
       
       Project: Gradle - Groovy Project
       
       사용 기능: web, thymeleaf, jpa, h2, lombok, validation
       
       groupId: jpabook
       
       artifactId: jpashop
       ```

   - 그래들 빌드 사양

     - ```groovy
       plugins {
       	id 'java'
       	id 'org.springframework.boot' version '2.7.13'
       	id 'io.spring.dependency-management' version '1.0.15.RELEASE'
       }
       
       group = 'jpabook'
       version = '0.0.1-SNAPSHOT'
       
       java {
       	sourceCompatibility = '11'
       }
       
       configurations {
       	compileOnly {
       		extendsFrom annotationProcessor
       	}
       }
       
       repositories {
       	mavenCentral()
       }
       
       dependencies {
       	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
       	implementation 'org.springframework.boot:spring-boot-starter-validation'
       	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
       	implementation 'org.springframework.boot:spring-boot-starter-web'
       	compileOnly 'org.projectlombok:lombok'
       	runtimeOnly 'com.h2database:h2'
       	annotationProcessor 'org.projectlombok:lombok'
       	testImplementation 'org.springframework.boot:spring-boot-starter-test'
       	//JUnit4 추가
       	testImplementation("org.junit.vintage:junit-vintage-engine") {
       		exclude group: "org.hamcrest", module: "hamcrest-core"
       	}
       }
       
       tasks.named('test') {
       	useJUnitPlatform()
       }
       ```

   - 라이브러리 살펴보기

   - View 환경 설정

     - HelloController
     
       - ```java
         package jpabook.jpashop;
         
         import org.springframework.stereotype.Controller;
         import org.springframework.ui.Model;
         import org.springframework.web.bind.annotation.GetMapping;
         
         @Controller
         public class HelloController {
         
             @GetMapping("/hello")
             public String hello(Model model) {
                 model.addAttribute("data", "hello!! world");
                 return "hello";
             }
         }
         ```
     
     - 스프링 부트의 경로설정
     
       - 스프링 부트 thymeleaf viewName 매핑
       - `resources:templates/` +`{ViewName}` + `.html`
       - `\resources\templates\hello.html`
     
     - index.html은 static 폴더에 만들기
     
     - Recompile TIP
     
       - ```
         html을 수정하고 서버를 켰다 껐다하는게 굉장히 귀찮기 때문에 이걸 없앨 방법이 존재함
         
         1. build.gradle의 dependencies에 " implementation 'org.springframework.boot:spring-boot-devtools' "를 추가한다
         
         2. 수정한 html 파일을 Build 메뉴의 recomplie을 클릭해서 새로고침하면 됨
         ```
     
       - ![image-20230710231610759](https://github.com/wooko5/JPA-Part1/assets/58154633/cd82c849-6cc0-4dd8-8606-002d8527c2c8)
   
   - H2 설정

     - 팁
       - Version 1.4.200를 사용해야한다
     - 사용법
       - ![image-20230711225014238](https://github.com/wooko5/JPA-Part1/assets/58154633/6b41955c-6f02-4015-8c23-8cbf2a27d8eb)
       - ![image-20230711225117511](https://github.com/wooko5/JPA-Part1/assets/58154633/e64f3145-6825-4f28-92b6-f22e3242d278)
         - 특정 파일(/jpashop.mv.db) 생성된걸 확인하고
       - ![image-20230711230057044](https://github.com/wooko5/JPA-Part1/assets/58154633/2b89118f-c59b-469f-bc28-5c025360f6b8)
   
   - JPA와 DB 설정 동작확인

     - application.yml 파일 설정

       - ```yaml
         spring:
           datasource:
             url: jdbc:h2:tcp://localhost/~/jpashop
             username: sa
             password:
             driver-class-name: org.h2.Driver
         
           jpa:
             hibernate:
               ddl-auto: create
             properties:
               hibernate:
                 show_sql: true
                 format_sql: true
         
         logging:
           level:
             org.hibernate.SQL: debug
             org.hibernate.type: trace
         ```
     
     - `spring.jpa.hibernate.ddl-auto: create`
     
       - 애플리케이션 실행 시점에 테이블을 drop 하고, 다시 생성
     
     - 오류
   
       - @Test에 Tractional 설정을 해주지 않았기 때문에 발생
       - ![image-20230711234321963](https://github.com/wooko5/JPA-Part1/assets/58154633/ebdff382-84a5-4425-8d05-59450c411d2e)
       - javax와 spring 중에서 spring 껄로 `@Transactional`을 추가
     
     - localhost:8082로 h2에 접속가능
     
       - ![image-20230711234623738](https://github.com/wooko5/JPA-Part1/assets/58154633/8e7694a4-0ed6-4cb1-8869-7565a16b8df2)
   
     - 빌드

       - ```groovy
         ./gradlew clean build
         
         cd build/libs
         
         java -jar jpashop-0.0.1-SNAPSHOT.jar
         ```
   
     - TIP - 쿼리 파라미터 남기기
     
       - ```yaml
         logging:
           level:
             org.hibernate.type: trace
         ```
     
       - ![image-20230712023134056](https://github.com/wooko5/JPA-Part1/assets/58154633/5844f8a9-5e76-4d28-b09f-88fcf3369f7d)
     
       - ```groovy
         implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6'
         ```
     
     - TIP - build.gradle
     
       - 가끔 보면 dependencies에 숫자로 버전이 있는 라이브러리가 있고 아닌게 있는데 스프링부트에서 해당 부트 버전에 맞는 라이브러리가 무엇인지 설정해놓은 라이브러리는 숫자버전이 없고, 지원하지 않는 라이브러리는 숫자가 있다
     
       - ```groovy
         /* 스프링부트 2.7.13에서 지원하는 라이브러리는 버전정보가 없고, 아니면 버전정보가 있다 */
         plugins {
         	id 'java'
         	id 'org.springframework.boot' version '2.7.13'
         	id 'io.spring.dependency-management' version '1.0.15.RELEASE'
         }
         
         
         dependencies {
         	implementation 'org.springframework.boot:spring-boot-starter-web'
         	implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6'
         }
         
         tasks.named('test') {
         	useJUnitPlatform()
         }
         
         ```
     
       - ![image-20230712023750764](https://github.com/wooko5/JPA-Part1/assets/58154633/83926249-20a1-4c00-9787-62e292c025e5)
     
     - **개발할 때 로그를 많이 남기는건 좋은 습관이지만 운영환경에서 로그를 많이 남기면 성능에 좋지 않기 때문에 항상 운영에서는 해당 로그가 성능이나 보안에 영향을 끼치는지 확인하고 설계해야한다**
     
       
   
2. 도메인 분석 설계(요구사항 분석)

   - 요구사항 분석

     - ```markdown
       기능 목록:
       	회원 기능:
       		회원 등록
       		회원 조회
       	상품 기능:
       		상품 등록
       		상품 수정
       		상품 조회
       	주문 기능:
       		상품 주문
       		주문 내역 조회
       		주문 취소
       	기타 요구사항:
       		상품은 재고 관리가 필요하다.
       		상품의 종류는 도서, 음반, 영화가 있다.
       		상품을 카테고리로 구분할 수 있다.
       		상품 주문시 배송 정보를 입력할 수 있다.
       ```

   - 도메인 모델과 테이블 설계

     -  도메인 모델

        -  ![image-20230712234814302](https://github.com/wooko5/JPA-Part1/assets/58154633/e7dab834-ff6a-446d-95cb-b28ff4fc1b47)
        -  many to many는 최대한 사용하지 말자
        -  단방향 관계를 지향하자

     -  회원 엔티티 분석(객체)

        -  ![image-20230713001456853](https://github.com/wooko5/JPA-Part1/assets/58154633/f135b524-808d-4f7d-94e6-b0fcb7820620)
        -  TIP - 회원이 주문을 하기 때문에, 회원이 주문리스트를 가지는 것은 얼핏 보면 잘 설계한 것 같지만, 객체 세상은 실제 세계와는 다르다. 
        -  실무에서는 회원이 주문을 참조하지 않고, 주문이 회원을 참조하는 것으로 충분하다.
        -  SQL 쿼리문을 짤 때도 특정 회원의 주문을 조회하고 싶을 때, 주문 테이블에서 조건문에 회원을 넣지 않는가
        -  여기서는 일대다, 다대일의 양방향 연관관계를 설명하기 위해서 추가했다

     -  회원 테이블 분석(RDB)

        -  ![image-20230713001839270](https://github.com/wooko5/JPA-Part1/assets/58154633/11865685-22aa-4928-bf72-a1bd15a4cb95)
        -  ITEM 테이블(싱글테이블 전략)
           -  DTYPE 컬럼으로 타입을 구분하기에 성능상 유리함(조인하지 않기에 조회 성능이 빠르고, 조회 쿼리가 단순)
           -  한 테이블에 다 때려박기에 테이블 크기가 너무 커지면 성능상 문제가 생길 수 있음
        -  **객체에서는 카테고리와 아이템을 다대다 관계로 만들 수 있지만, RDB에서는 일반적인 설계로는 불가능해서 중간에 매핑 테이블을 넣어야함**

     -  연관관계 매핑 분석

        - ```
          회원과 주문: 
          일대다, 다대일의 양방향 관계다. 따라서 연관관계의 주인을 정해야 하는데, 외래 키가 있는 주문을 연관관계의 주인으로 정하는 것이 좋다. 그러므로 Order.member를 ORDERS.MEMBER_ID 외래 키와 매핑한다.
          
          
          주문상품과 주문: 
          다대일 양방향 관계다. 외래 키가 주문상품에 있으므로 주문상품이 연관관계의 주인이다. 그러므로 OrderItem.order를 ORDER_ITEM.ORDER_ID 외래 키와 매핑한다.
          
          
          주문상품과 상품: 
          다대일 단방향 관계다. OrderItem.item 을 ORDER_ITEM.ITEM_ID 외래 키와 매핑한다.
          
          
          주문과 배송: 
          일대일 양방향 관계다. Order.delivery 를 ORDERS.DELIVERY_ID 외래 키와 매핑한다.
          
          
          카테고리와 상품: 
          @ManyToMany 를 사용해서 매핑한다.(실무에서 @ManyToMany는 사용하지 말자. 여기서는 다대다 관계를 예제로 보여주기 위해 추가했을 뿐이다)
          ```

        - 일대다 관계에서 항상 '다'인 쪽이 FK를 갖게 되므로 연관관계의 주인 엔티티가 된다

        - 참고

          -  외래 키가 있는 곳을 연관관계의 주인으로 정한다
          -  연관관계의 주인은 단순히 외래 키를 누가 관리하냐의 문제이지 비즈니스상 우위에 있다고 주인으로 정하면 안 된다
     
   - 엔티티 클래스 개발

     - FK(외래키)

       - 생성하면 데이터의 정합성을 보장할 수 있고
         - 실시간 성능보다 돈처럼 데이터의 정합성을 보장할 수 있어야한다면 채택
       - 없으면 정합성을 보장할 수 없지만 인덱스만 잘 생성하면 성능이 빨라질 수 있다
         - 돈 계산보다 실시간 성능이 중요한 거라면 채택

     - 엔티티의 getter, setter

       - getter 같은 경우 많이 사용해도 상관없지만 setter를 막 열어두면 엔티티가 어디서 변경됐는지 추적하기 힘들어짐
       - 그렇기 때문에 변경 지점이 명확한 별도의 메소드를 제공하는 것이 안전하다

     - @Column(name = "member_id")

       - 객체는 Member같은 클래스 타입이 있어서 id 필드 만으로 쉽게 구별이 가능
       - 하지만 테이블은 타입이 없으므로 'member_id'같은 칼럼명을 사용함으로써 나중에 테이블 조인 시, 헷갈리지 않게 한다

     - @ManyToMany는 사용 X

       - > @ManyToMany 는 편리한 것 같지만, 중간 테이블( CATEGORY_ITEM )에 컬럼을 추가할 수 없고, 세밀하게 쿼리를 실행하기 어렵기 때문에 실무에서 사용하기에는 한계가 있다. 
         >
         > 
         >
         > 중간 엔티티로 CategoryItem 를 만들고 @ManyToOne , @OneToMany 로 매핑해서 사용하자. 
         >
         > 
         >
         > 정리하면 다대다 매핑을 일대다, 다대일 매핑으로 풀어내서 사용하자.

     - 값 타입(@Embeddable)은 변경 불가능하게 설계해야 한다

       - > @Setter 를 제거하고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들자. 
         >
         > 
         >
         > JPA 스펙상 엔티티나 임베디드 타입( @Embeddable )은 자바 기본 생성자(default constructor)를 public 또는 protected 로 설정해야 한다. 
         >
         > 
         >
         > public 으로 두는 것 보다는 protected 로 설정하는 것이 그나마 더 안전하다.
         >
         > 
         >
         > JPA가 이런 제약을 두는 이유는 JPA 구현 라이브러리가 객체를 생성할 때 리플랙션 같은 기술을 사용할 수 있도록 지원해야 하기 때문이다.

   - 엔티티 설계시 주의점

     - **실무에서는 엔티티에 가급적 Setter를 사용하지 말자**

       - 엔티티에 setter가 열려있으면 변경 포인트가 너무 많아지기 때문에 유지/보수가 어렵다

     - **모든 연관관계는 지연로딩으로 설정한다!!!**

       - ```markdown
         1. 즉시로딩(EAGER)은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다. 특히 JPQL을 실행할 때 N+1 문제가 자주 발생한다.
         
         2. 실무에서 모든 연관관계는 지연로딩(LAZY)으로 설정해야 한다.
         
         3. 연관된 엔티티를 함께 DB에서 조회해야 하면, fetch join 또는 엔티티 그래프 기능을 사용한다.
         
         4. @XxxxToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩이므로 직접 지연로딩으로 설정해야 한다.
         
         5. @ManyToMany, @OneToMany는 기본이 LAZY임
         ```
         
       - 즉시로딩(EAGER)은 특정 엔티티를 조회할 때, 해당 엔티티와 관련있는 다른 엔티티의 정보도 같이 조회한다

       - 예를 들어, Member와 Order는 1대다 관계이다. 여기서 주인은 Member를 FK로 가지고 있는 Order인데 Order의 데이터를 가져오고 싶을 때 FetchType을 즉시로딩(EAGER)로 하면 Order뿐만 아니라 Member의 정보도 같이 가지고 온다

       - 지연로딩(LAZY)는 특정 엔티티를 조회할 때, 해당 엔티티의 정보만 조회하고 연관된 다른 엔티티의 정보를 가져오지 않는다. 대신 실제로 다른 엔티티에 접근할 때 프록시를 통해 가져온다.

       - `N+1 문제`
     
         - N + 1 문제는 연관관계가 설정된 엔티티 사이에서 한 엔티티를 조회하였을 때, 조회된 엔티티의 개수(N 개)만큼 연관된 엔티티를 조회하기 위해 추가적인 쿼리가 발생하는 문제
         - 예를 들어, Member와 Order(주인)가 일대다 연관관계이다. 
         - Order를 전체 조회하는 쿼리를 호출한다 ==> Member 조회 쿼리가 Member의 조회 데이터(row) 개수만큼 로그에 발생한다 
         - 즉 Member의 데이터 개수가 4개이고/ Order 데이터를 조회할 때, Order 데이터 조회를 위한 쿼리 1개랑 Member를 조회하는 4개(Member의 데이터 개수)의 쿼리가 실행되어서 총 1+4개의 쿼리가 로그에 출력된다. 이를 `N+1 문제`라고 한다 
     
     - **컬렉션은 필드에서 초기화하자**
     
       - ```markdown
         컬렉션은 필드에서 바로 초기화 하는 것이 안전하다.
         
         null 문제에서 안전하다.
         
         하이버네이트는 엔티티를 영속화 할 때, 컬랙션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다. 
         
         만약 getOrders() 처럼 임의의 메서드에서 컬력션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있다. 
         
         Hibernate는 컬렉션을 효율적으로 관리하기 위해 엔티티를 영속 상태로 만들 때, 원본 컬렉션을 감싸고 있는 내장 컬렉션을 생성해서 내장 컬렉션을 사용하도록 참조를 변경한다.
         
         Hibernate가 제공하는 내장 컬렉션(PersistentBag, PersistentSet, PersistentList)은 원본 컬렉션을 감싸고 있어 래퍼 컬렉션이라고도 부른다.
         
         따라서 필드레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다.
         ```
         
       - ```java
         Member member = new Member();
         System.out.println(member.getOrders().getClass());
         
         em.persist(member);
         System.out.println(member.getOrders().getClass());
         
         /* 출력 결과 */
         class java.util.ArrayList
         class org.hibernate.collection.internal.PersistentBag
         ```
     
     - **테이블명, 칼럼명 생성 전략**
     
       - 스프링 부트에서 하이버네이트 기본 매핑 전략을 변경해서 실제 테이블 필드명은 다름
     
       - 참조
     
         - https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/htmlsingle/#howtoconfigure-hibernate-naming-strategy
         - http://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#naming
     
       - 하이버네이트 기존 구현: 엔티티의 필드명을 그대로 테이블의 컬럼명으로 사용
     
         - SpringPhysicalNamingStrategy
     
       - 스프링 부트 신규 설정 (엔티티(필드) ==> 테이블(컬럼))
     
         - ```tex
           1. 카멜 케이스 언더스코어(memberPoint member_point)
           
           2. .(점) _(언더스코어)
           
           3. 대문자 소문자
           ```
     
         - ![image](https://github.com/wooko5/JPA-Part1/assets/58154633/6e92bf4c-22c6-48d2-a2bb-b8dba38a0fbe)
     
     - `cascade = CascadeType.ALL`
     
       - 개념
         - cascade 옵션이란 Entity의 상태 변화를 전파시키는 옵션이다
         - @OneToMany 나 @ManyToOne에 옵션으로 줄 수 있음
         - 예를 들어, 두 엔티티 Order, OrderItem이 존재하고 일대다의 양방향 연관관계일 때, Order 엔티티를 persist() 할 때 OrderItem 엔티티도 연쇄해서 영속화할 때 사용한다
     
       - ```java
         @Entity
         @Table(name = "orders")
         @Getter @Setter
         public class Order {
             @Id
             @GeneratedValue
             @Column(name = "order_id")
             private Long id;
         
             @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 영속성 전이 옵션 추가
             private List<OrderItem> orderItems = new ArrayList<>();
             
             public void addOrderItem(OrderItem orderItem){
                 orderItems.add(orderItem);
                 orderItem.setOrder(this);
             }
         }
         
         @Entity
         @Getter @Setter
         public class OrderItem {
         
             @Id
             @GeneratedValue
             @Column(name = "order_item_id")
             private Long id;
             
             @ManyToOne(fetch = FetchType.LAZY) // 모든 연관관계는 지연로딩으로 설정
             @JoinColumn(name = "order_id")
             private Order order;
         
             private int orderPrice; // 주문 가격
             private int count; // 주문 수량
         }
         
         public Class Test{
             public static void main(String[] args) {
                 Order order = new Order();
                 OrderItem OrderItemA = new OrderItem();
                 OrderItem OrderItemB = new OrderItem();
                 order.addOrderItem(OrderItemA);
                 order.addOrderItem(OrderItemB);
                 
                 /* CascadeType.ALL 옵션을 없다면, 기존에는 아래처럼 저장할 때 3줄의 코드를 넣었다. */
                 entityManager.persist(order);
                 entityManager.persist(OrderItemA);
                 entityManager.persist(OrderItemB);
                 
                 /* 하지만 CascadeType.ALL 옵션이 있다면, 1줄로 '영속성 전이'를 할 수 있다. */
                 entityManager.persist(order);
             }
         }
         ```
         
       - https://hongchangsub.com/jpa-cascade-2/
     
     - 연관관계 편의 메소드
     
       - 엔티티(A, B)가 양방향 연관관계일 때, 특정 엔티티(A)에서 두 엔티티의 양방향 연관관계를 설정해주는 메소드를 `연관관계 편의 메소드`라고 한다
       
       - 주의 사항
       
         - 잘못된 연관관계 설정으로 인해 무한루프에 빠지지 않도록 조심하자!!!
         - 1대다 관계에서 1의 엔티티에서 연관관계 편의 메소드를 작성할 때, 기존 연관관계는 끊어줘야 안전하다
       
       - ```java
         /* 
         Member와 Order는 1대다 관계이다
         즉 Order는 Member를 FK로 갖고있는 연관관계 주인 엔티티다
         연관관계 편의 메소드는 웬만하면 연관관계 주인 엔티티에서 생성하는 것이 편하다
         */
         
         @Entity
         @Getter @Setter
         public class Member {
         
             @Id
             @GeneratedValue
             @Column(name = "member_id")
             private Long id;
             
             @OneToMany(mappedBy = "member")
             private List<Order> orders = new ArrayList<>(); // 컬렉션은 필드에서 초기화 하자.
             
             /* 일대다 연관관계의 일측(Member)에서 연관관계를 지정할 때 기존 연관관계는 끊어주어야 한다. */
         //    public void setOrder(Order order) {
         //        this.orders.add(order);
         //        if (order.getMember() != this) {
         //            order.setMember(this);
         //        }
         //    }
         }
         
         
         @Entity
         @Table(name = "orders")
         @Getter @Setter
         public class Order {
             @Id
             @GeneratedValue
             @Column(name = "order_id")
             private Long id;
         
             // 연관관계의 주인으로 본다
             @ManyToOne(fetch = FetchType.LAZY) // 모든 연관관계는 특별한 경우를 제외하고 지연로딩으로 설정
             @JoinColumn(name = "member_id")
             private Member member;
             
             /* 연관관계 편의 메소드 생성, 양방향 관계에서 주인쪽(FK소유)에 메소드를 만들어주는게 좋다*/
             public void setMember(Member member){
                 this.member = member;
                 member.getOrders().add(this);
             }
         }
         ```
       
         

3. 애플리케이션 구현 준비

   - 구현 요구사항

     - 예제를 단순화 하기 위해 다음 기능은 구현 X
       - 로그인과 권한 관리X
       - 파라미터 검증과 예외 처리X
       - 상품은 도서만 사용
       - 카테고리는 사용X
       - 배송 정보는 사용X

   - 애플리케이션 아키텍쳐

     - 아키텍쳐
       - ![image-20230717234207409](https://github.com/wooko5/JPA-Part1/assets/58154633/42b4e8ac-97ed-405f-84c1-e3bb96b9528f)

     - 계층형 구조

       - ```markdown
         controller, web: 웹계층
         
         service: 비즈니스 로직, 트랜잭션 처리
         
         repository: JPA를 직접 사용하는 계층, 엔티티 매니저 사용
         
         domain: 엔티티가 모여 있는 계층, 모든 계층에서 사용
         ```

     - 패키지 구조(jpabook.jpashop)

       - domain
       - exception
       - repository
       - service
       - web

     - 개발 순서

       - 서비스와 리포지토리 계층을 먼저 개발하고 (1)
       - 이후에 테스트 케이스를 작성해서 검증하고 (2)
       - 마지막에 웹 계층을 적용한다 (3)

4. 회원 도메인 개발

   - 회원 레포지토리 개발

     - `@SpringBootApplication`

       - 해당 패키지에 있거나 하위 패키지에 있는 컴포넌트들을 자동 스캔해서 스프링 빈으로 등록해준다

     - JPQL과  SQL의 차이점

       - SQL은 테이블을 대상으로한 쿼리지만 
       - JPQL은 엔티티(객체)를 대상으로 하는 쿼리

     - JPQL 예시

       - ```java
         public List<Member> findAll() {
             return entityManager.createQuery("SELECT m FROM Member m", Member.class).getResultList();
         }
         
         public List<Member> findByName(String username) {
             return entityManager.createQuery("SELECT m FROM Member m WHERE m.name = :name", Member.class)
                 .setParameter("name", username)
                 .getResultList();
         }
         ```

   - 회원 서비스 개발

     - @Transactional(readOnly = true)

       - 트랜잭션에서 읽기 전용 서비스 로직임을 표현함으로써 JPA가 최적화된다

       - DB마다 다르지만 해당 로직에 대해 단순 읽기로 처리하기 위해 리소스를 덜 쓰는 등의 최적화가 자동적으로 발생한다

       - ```java
         /* 회원 단건 조회 */
         @Transactional(readOnly = true)
         public Member findOne(Long id){
             return memberRepository.findOne(id);
         }
         ```

       - 디폴트는 false이다.

         - @Transactional(readOnly = false) == @Transactional

     - @Autowired 필드주입 주의점

       - ```java
         /* 아래 두 개는 지양하자! */
         
         @Autowired // 필드주입
         private MemberRepository memberRepository;
         
         private MemberRepository memberRepository;
         
         // setter 주입, 런타임 시 누군가 수정할 수 있어서 위험
         @Autowired
         public void setMemberRepository(MemberRepository memberRepository) {
             this.memberRepository = memberRepository;
         }
         ```

     - 생성자 주입 방식을 권장

       - ```java
         private final MemberRepository memberRepository;
         
         public MemberService(MemberRepository memberRepository) {
             this.memberRepository = memberRepository;
         }
         ```
     
     - `@RequiredArgsConstructor`을 쓰면 final로 초기화가 꼭 되어야하는 필드들만 따로 생성자 주입을 통해 파라미터로 이용하기에 개발자가 실수하지 않을 확률이 높아진다 
     
       - ```java
         @PersistenceContext
         private EntityManager entityManager;
         
         //기존에 쓰던 위의 코드를 @RequiredArgsConstructor를 추가하면 간단하게 EntityManager를 주입할 수 있다 
         private final EntityManager entityManager;
         ```
     
       - 원래는 `@PersistenceContext`를 선언해야 `EntityManager`를 쓸 수 있는데 Spring Data JPA가 지원해줘서 생성자 주입만으로도 쓸 수 있는 것, 추후에는 스프링 기본 라이브러리에서도 가능하게 추가될 예정이라고 한다
       
     
   - 회원 기능 테스트

     - 테스트 요구사항

       - 회원 가입을 성공해야 한다
       - 회원 가입할 때, 같은 이름이 있으면 예외처리를 한다

     - 기술설명

       - ```markdown
         @RunWith(SpringRunner.class): 스프링과 테스트 통합
         
         @SpringBootTest: 스프링 부트 띄우고 테스트(이게 없으면 @Autowired 다 실패)
         
         @Transactional: 반복 가능한 테스트 지원, 각각의 테스트를 실행할 때마다 트랜잭션을 시작하고 테스트
         가 끝나면 트랜잭션을 강제로 롤백 (이 어노테이션이 테스트 케이스에서 사용될 때만 롤백)
         ```

         

     - JPA 실행 시, 저장 테스트에서 로그에 INSERT문이 안 나오는 이유

       - ```java
         @RunWith(SpringRunner.class)
         @SpringBootTest
         @Transactional
         public class MemberServiceTest {
         
             @Autowired
             private MemberRepository memberRepository;
         
             @Autowired
             private MemberService memberService;
         
             @Test
             @Rollback(false)
             public void 회원가입() throws Exception{
                 //given
                 Member member = new Member();
                 member.setName("Jaeuk");
         
                 /**
                  * 실행 로그를 보면 저장테스트인데 DB에 INSERT문을 실행하지 않는다.
                  * 그 이유는 기본적으로 JPA에서 em.persist(member)를 한다해서 DB에 INSERT문을 실행하는게 아니기 때문이다
                  * 영속성 컨텍스트에 저장된 객체 데이터가 commit, flush가 실행되면 INSERT문을 생성하면서 DB에 전달되는 구조이기 때문이다
                  *
                  * commit, flush가 실행 안 되는 이유: @Transactional가 rollback이 기본적으로 true이기 때문이다
                  * 그래서 해당 메소드에 @Rollback(false)로 두면 insert문이 실행된다
                  */
                 Long savedId = memberService.join(member); //when
         
                 //then
                 Assertions.assertEquals(member, memberRepository.findOne(savedId));
             }
         }
         ```

       - ```java
         @RunWith(SpringRunner.class)
         @SpringBootTest
         @Transactional
         public class MemberServiceTest {
         
             @Autowired
             private MemberRepository memberRepository;
         
             @Autowired
             private MemberService memberService;
             
             @Autowired
             private EntityManager entityManager; // 새로추가됨
         
             @Test
             public void 회원가입() throws Exception{
                 //given
                 Member member = new Member();
                 member.setName("Jaeuk");
                 
                 //when
                 Long savedId = memberService.join(member); 
                 
                 // @Rollback(false)을 쓰지않고 DB에 INSERT문을 넣고 싶다면 flush()을 추가하자
                 entityManager.flush();
         
                 //then
                 Assertions.assertEquals(member, memberRepository.findOne(savedId));
             }
         }
         ```

     - 중복회원_예외 테스트

       - ```java
         @Test
         public void 중복_회원_예외() throws Exception {
             //given
             Member memberA = new Member();
             memberA.setName("Oh");
         
             Member memberB = new Member();
             memberB.setName("Oh");
         
             //when
             memberService.join(memberA);
             try {
                 memberService.join(memberB);
             } catch (IllegalStateException e) {
                 return;
             }
         
             //then
             fail("예외가 발생해야한다!!!");
         }
         
         /*=========아래는 개선된 코드 ========*/
         @Test(expected = IllegalStateException.class)
             public void 중복_회원_예외() throws Exception {
                 //given
                 Member memberA = new Member();
                 memberA.setName("Oh");
         
                 Member memberB = new Member();
                 memberB.setName("Oh");
         
                 //when
                 memberService.join(memberA);
                 memberService.join(memberB);
         
                 //then
                 fail("예외가 발생해야한다!!!");
         }
         ```

     - 테스트 케이스를 위한 설정

       - test폴더에 resources 폴더를 만들고, application.yml을 새로 생성해서 기존의 application.yml과 다른 설정을 해주면 테스트 시에만 쓰는 환경이 만들어진다

       - [H2경로](https://h2database.com/html/main.html)에 들어가서 [Cheat Sheet](https://h2database.com/html/cheatSheet.html)을 누르고 해당 경로를 가져온다

       - ![image-20230721021906101](https://github.com/wooko5/JPA-Part1/assets/58154633/be897e6f-5b83-40b6-967d-221da03a3ba1)

       - ```yaml
         spring:
           datasource:
             url: jdbc:h2:mem:test # url 경로를 in-memory 환경으로 변경했다
             username: sa
             password:
             driver-class-name: org.h2.Driver
         
           jpa:
             hibernate:
               ddl-auto: create-drop # create는 기존에 있던 엔티티를 다 날리고 시작, create-drop은 시작은 같지만 마지막에 모든 엔티티를 날려버림
             properties:
               hibernate:
         #        show_sql: true
                 format_sql: true
         
         logging:
           level:
             org.hibernate.SQL: debug
             org.hibernate.type: trace
         ```

       - 기본적으로 설정이 없다면 인메모리로 돌리는게 디폴트이기 때문에 설정 안 해줘도 돌아간다(심지어 yml 파일없어도 돌아감)

       - ![image-20230721022101826](https://github.com/wooko5/JPA-Part1/assets/58154633/e4687ffb-9801-4888-a687-23cbed2c545c)

5. 상품 도메인 개발

   - 상품 엔티티 개발

     - 구현기능
       - 상품등록/상품목록조회/상품수정

   - 상품 레포지토리 개발

     - `entityManager.merge(item)`

       - ```
         merge()는 준영속성 상태의 엔티티를 영속 상태로 변경할 때 사용한다
         
         쉽게 말하자면, 저장할 Item 엔티티와 PK가 동일한 엔티티가 DB에 존재하면 해당 엔티티를 update처럼 값을 변경해준다
         
         단점 : 엔티티를 변경할 때, 어떤 속성값이 초기화 되어있지 않다면 DB에 NULL로 저장됨
         
         WEB에서 더 자세하게 알아본다
         ```

   - 상품 서비스 개발

     - 완료

6. 주문 도메인 개발

   - 주문, 주문 상품 엔티티 개발

     - 구현기능

       - ```markdown
         상품 주문
         주문 내역 조회
         주문 취소
         ```

     - Order 비즈니스 로직 생성

       - createOrder

         - ```java
           /* 비즈니스 로직 - 생성 메서드, OrderItem...은 여러개를 의미한다 */
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
           ```

       - cancle

         - ```java
           /* 비즈니스 로직 - 주문취소 메서드 */
           public void cancle() {
               if (delivery.getDeliveryStatus() == DeliveryStatus.COMPLETED) {
                   throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
               }
               this.setOrderStatus(CANCLE);
               /**
               * this.orderItems로 써도 되지만 생략하는 이유:
               * 이미 IDE에서 해당 orderItems로만 써도 보라색으로 색칠하기 때문에 식별하기 쉬워서 굳이 this.orderItems로 쓰지 않았다.
               * 개인 취향의 문제이지만 주로 1.강조하고 싶을 때, 2.변수 이름이 중복될 때를 제외하고는 잘 안 쓴다.
               */
               for (OrderItem orderItem : orderItems) {
                   orderItem.cancel(); // orderItem의 재고 수량을 원복하는 메소드 처리
               }
           }
           ```

       - getTotalPrice

         - ```java
           /* 조회 로직 - 전체 주문 가격을 조회하는 메서드 */
           public int getTotalPrice() {
               int totalPrice = 0;
               for (OrderItem orderItem : orderItems) {
                   totalPrice += orderItem.getTotalPrice();
               }
               return totalPrice;
               /* stream 방식으로 하는 방법도 존재 
               return orderItems.stream()
                   .mapToInt(OrderItem::getTotalPrice)
                   .sum();
                */
           }
           ```

     - OrderItem 비즈니스 로직 생성

       - ```java
         /* 생성 메서드 */
         public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
             OrderItem orderItem = new OrderItem();
             orderItem.setItem(item);
             orderItem.setOrderPrice(orderPrice);
             orderItem.setCount(count);
             item.removeStock(count); // 넘어온 count만큼 item에서 재고(stockQuantity)를 제거해줌
             return orderItem;
         }
         
         /* 비즈니스 로직 - 재고 수량을 원복해주는 메서드 */
         public void cancel() {
             getItem().addStock(count); // this.getItem().addStock(count);으로 작성할 수 있지만 헷갈릴 getter가 없어서 간단하게 작성
         }
         
         /* 비즈니스 로직 - 주문의 전체 가격을 반환하는 메서드 */
         public int getTotalPrice() {
             return getOrderPrice() * getCount(); // 주문가격 * 수량 == 주문 전체 가격
         }
         ```

   - 주문 레포지토리 개발

     - 완료

   - 주문 서비스 개발

     - 주문 로직 개발

     - 취소 로직 개발

     - 검색 로직 개발

     - TIP

       - > 주문 서비스의 주문과 주문 취소 메서드를 보면 비즈니스 로직 대부분이 엔티티에 있다.
         >
         >  
         >
         > 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다. 
         > 
         >
         > 이처럼 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 도메인 모델 패턴(http://martinfowler.com/eaaCatalog/domainModel.html)이라 한다. 
         >
         > 
         >
         > 반대로 엔티티에는 비즈니스 로직이 거의 없고 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을 트랜잭션 스크립트 패턴(http://martinfowler.com/eaaCatalog/transactionScript.html)이라 한다.
         >
         > 
         >
         > 심지어 같은 프로젝트 안에서 도메인 모델 패턴과 트랜잭션 스크립트 패턴을 동시에 쓰기도 한다.

   - 주문 기능 테스트

     - TIP
       - 좋은 테스트란 JPA나 스프링 의존성 주입 없이 순수하게 테스트 메소드만으로 검증하는게 좋다

   - 주문 검색 기능 개발

     - 개요

       - JPA에서 동적 쿼리를 어떻게 생성하는지 알기위해 배운다
       - 동적쿼리로 JPQL/JPA Criteria보다 QueryDsl을 실무에서 많이 쓰지만 교육적인 목적으로 이런게 있다는 식으로 알아만두자

     - JPQL 방식

       - ```java
         /**
         * JPQL(동적쿼리)
         * 너무 길고, 복잡해서 실무에서 잘 쓰지 않는다
         */
         public List<Order> findAllByString(OrderSearch orderSearch) {
             StringBuilder jpql = new StringBuilder("SELECT o FROM Order o join o.member m");
             boolean isFirstCondition = true;
         
             //주문 상태 검색
             if (orderSearch.getOrderStatus() != null) {
                 if (isFirstCondition) {
                     jpql.append(" WHERE");
                     isFirstCondition = false;
                 } else {
                     jpql.append(" AND");
         
                 }
                 jpql.append(" o.status = :status");
             }
         
             //회원 이름 검색
             if (StringUtils.hasText(orderSearch.getMemberName())) {
                 if (isFirstCondition) {
                     jpql.append(" WHERE");
                     isFirstCondition = false;
                 } else {
                     jpql.append(" AND");
                 }
                 jpql.append(" m.name LIKE :name");
             }
         
             TypedQuery<Order> query = entityManager.createQuery(jpql.toString(), Order.class).setMaxResults(1000); //최대 1000건
         
             if (orderSearch.getOrderStatus() != null) {
                 query.setParameter("status", orderSearch.getOrderStatus());
             }
             if (StringUtils.hasText(orderSearch.getMemberName())) {
                 query.setParameter("name", orderSearch.getMemberName());
             }
         
             return query.getResultList(); // JPQL 쿼리를 문자로 생성하기는 번거롭고, 실수로 인한 버그가 충분히 발생할 수 있다!!!
         }
         ```

     - JPA Criteria

       - ```java
         /**
         * JPA Criteria(동적쿼리)
         * 아까보다 그나마 나은 방법이지만 QueryDSL보다는 어렵고, 실무에서 잘 쓰지 않는다.
         * 유지/보수가 제로에 가깝다. 왜냐하면 보자마자 어떤 Query인지 감이 안 오기 때문
         */
         public List<Order> findALLByCriteria(OrderSearch orderSearch) {
             CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
             CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
             Root<Order> orderRoot = criteriaQuery.from(Order.class);
             Join<Order, Member> memberJoin = orderRoot.join("member", JoinType.INNER); //회원과 조인
         
             List<Predicate> criteria = new ArrayList<>();
         
             //주문 상태 검색
             if (orderSearch.getOrderStatus() != null) {
                 Predicate status = criteriaBuilder.equal(orderRoot.get("status"), orderSearch.getOrderStatus());
                 criteria.add(status);
             }
         
             //회원 이름 검색
             if (StringUtils.hasText(orderSearch.getMemberName())) {
                 Predicate name = criteriaBuilder.like(memberJoin.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
                 criteria.add(name);
             }
         
             criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[criteria.size()])));
             TypedQuery<Order> query = entityManager.createQuery(criteriaQuery).setMaxResults(1000);
             return query.getResultList();
         }
         ```

7. 웹 계층 개발

   - 홈 화면과 레이아웃

     - `th:replace` 

       - `th:replace="fragments/~"`는 해당 부분을 fragment 디렉터리의 html파일로 대체 선언한다는 의미
       - 레이아웃에서 공통적으로 적용되는 header나 footer 같은 경우, 모든 html 페이지마다 복붙해서 사용하기에는 너무 비효울적이다. 
       - 그래서 fragments directory를 생성해서 레이아웃에서 공통적으로 쓸 부분들의 html 코드를 생성한다

     - Hierarchical-style layouts

       > 예제에서는 뷰 템플릿을 최대한 간단하게 설명하려고, header , footer 같은 템플릿 파일을 반복해서 포함한다. 즉 'Include-style layouts'을 적용했다.
       >
       > Hierarchical-style layouts을 참고하면 home.html에 보이는 header, footer 코드의 중복을 제거할 수 있다.
       >
       > 
       >
       > https://www.thymeleaf.org/doc/articles/layouts.html

     - 부트스트랩 v4.3.1

       - `src/main/resources/static` 경로에 css, js 폴더 복붙하고, 서버 재시작
       - 오류 시
         - `src/main/resources`를 싱크로나이즈 or Build 메뉴의 `build project`하면 된다
       
     - 추가파일

       - `jumbotron-narrow.css`라는 파일을 `static/css` 폴더에 생성

       - ```css
         /* Space out content a bit */
         body {
             padding-top: 20px;
             padding-bottom: 20px;
         }
         /* Everything but the jumbotron gets side spacing for mobile first views */
         .header,
         .marketing,
         .footer {
             padding-left: 15px;
             padding-right: 15px;
         }
         /* Custom page header */
         .header {
             border-bottom: 1px solid #e5e5e5;
         }
         /* Make the masthead heading the same height as the navigation */
         .header h3 {
             margin-top: 0;
             margin-bottom: 0;
             line-height: 40px;
             padding-bottom: 19px;
         }
         /* Custom page footer */
         .footer {
             padding-top: 19px;
             color: #777; border-top: 1px solid #e5e5e5;
         }
         /* Customize container */
         @media (min-width: 768px) {
             .container {
                 max-width: 730px;
             }
         }
         .container-narrow > hr {
             margin: 30px 0;
         }
         /* Main marketing message and sign up button */
         .jumbotron {
             text-align: center;
             border-bottom: 1px solid #e5e5e5;
         }
         .jumbotron .btn {
             font-size: 21px;
             padding: 14px 24px;
         }
         /* Supporting marketing content */
         .marketing {
             margin: 40px 0;
         }
         .marketing p + h4 {
             margin-top: 28px;
         }
         /* Responsive: Portrait tablets and up */
         @media screen and (min-width: 768px) {
             /* Remove the padding we set earlier */
             .header,
             .marketing,
             .footer {
                 padding-left: 0;
                 padding-right: 0; }
             /* Space out the masthead */
             .header {
                 margin-bottom: 30px;
             }
             /* Remove the bottom border on the jumbotron for visual effect */
             .jumbotron {
                 border-bottom: 0;
             }
         }
         ```

   - 회원 등록

     - 회원 등록 폼 객체

       - ```java
         @Getter
         @Setter
         public class MemberForm {
             @NotEmpty(message = "회원 이름은 공백일 수 없습니다.")
             private String name;
             private String city;
             private String street;
             private String zipcode;
         }
         ```

     - 회원 등록 컨트롤러

       - ```java
         @Controller
         @RequiredArgsConstructor
         public class MemberController {
         
             private final MemberService memberService;
         
             @GetMapping("/members/new")
             public String createForm(Model model) {
                 model.addAttribute("memberForm", new MemberForm());
                 return "members/createMemberForm";
             }
         
             @PostMapping("/members/new")
             public String create(@Valid MemberForm form, BindingResult result) {
                 //@Valid를 통해 MemberForm에 이상한 값이 들어갔는지 검증 ==> name이 @NotEmpty이기 때문에 name이 공백인지 아닌지 검증
                 if(result.hasErrors()){ //BindingResult 없을 때는 error 페이지가 나왔지만 BindingResult가 있다면 error를 핸들링할 수 있음 ==> createMemberForm.html을 유심히 보자
                     return "members/createMemberForm";
                 }
         
                 Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
                 Member member = new Member();
                 member.setName(form.getName());
                 member.setAddress(address);
                 memberService.join(member);
                 return "redirect:/"; //SpringMVC1에서 배운 PRG 패턴 적용으로 무분별한 중복 POST 요청 방지!!!
             }
         }
         ```

     - MemberForm을 쓰는 이유

       - 단순하게 비슷한 Member 엔티티를 써도 되는 것처럼 느껴지지만,
       - controller 및 화면에서 넘어오는 validation과 도메인 Entity validation은 서로 상이할 가능성이 매우 높음
       - 화면에서 쓰는 폼 데이터와 관련 엔티티가 딱 맞아 떨어지는 경우가 실무에서는 매우 희박하다. 
       - 그래서 폼 데이터 전송만을 위한 객체나 DTO를 따로 설계하는게 좋다

     - 해야할 일

       - javax.validation 공부하기
       - [스프링과 타임리프 validation 연동](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#validation-and-error-messages)

   - 회원 목록 조회

     - 회원 목록 조회 컨트롤러

       - ```java
         @Controller
         @RequiredArgsConstructor
         public class MemberController {
         
             private final MemberService memberService;
             
             /**
              * 조회한 상품을 뷰에 전달하기 위해 스프링 MVC가 제공하는 모델(Model)객체에 보관
              * 실행할 뷰 이름을 반환
              */
             @GetMapping("/members")
             public String list(Model model){
                 /**
                  * 화면에서는 엔티티를 사용해도 괜찮지만, API에서는 절대 엔티티를 반환해서는 안 된다.
                  * 왜냐하면 API는 명세이기 때문에 엔티티의 속성을 추가하면 또 작성해줘야하고, 비밀번호같은 속성이 추가되면 보안이유도 존재한다.
                  * 그래서 교육이니깐! 단순하게 엔티티인 Member를 반환했지만, DTO를 사용해서 정말 필요한 데이터만 있는 객체를 활용하자
                  */
                 List<Member> members = memberService.findMembers();
                 model.addAttribute("members", members);
                 return "members/memberList";
             }
         }
         ```

     - JPA 사용 시 주의점

       - Entity를 최대한 순수하게 유지

         - 실무에 가면 요구사항이 단순하지 않아서 Entity 만으로 모든 요청/응답을 처리하기 정말 힘듦

         - 그러므로 DTO나 VO를 만들어서 해당 페이지나 요구사항에 맞는 데이터 전달 객체를 만들어서 Entity는 핵심 비즈니스 로직만 가지고 순수하게 설계해야한다

         - ```
           > 참고: 폼 객체 vs 엔티티 직접 사용
           
           > 참고: 요구사항이 정말 단순할 때는 폼 객체( MemberForm ) 없이 엔티티( Member )를 직접 등록과 수정 화면에서 사용해도 된다. 하지만 화면 요구사항이 복잡해지기 시작하면, 엔티티에 화면을 처리하기 위한 기능이점점 증가한다. 
           
           결과적으로 엔티티는 점점 화면에 종속적으로 변하고, 이렇게 화면 기능 때문에 지저분해진 엔티티는 결국 유지보수하기 어려워진다.
           
           > 실무에서 엔티티는 핵심 비즈니스 로직만 가지고 있고, 화면을 위한 로직은 없어야 한다. 
           화면이나 API에 맞는 폼 객체나 DTO를 사용하자. 
           그래서 화면이나 API 요구사항을 이것들로 처리하고, 엔티티는 최대한 순수하게 유지하자.
           ```

           

   - 상품 등록

     - 상품(Book) 폼 객체 만들기

       - ```java
         @Getter
         @Setter
         public class BookForm {
             //TODO: 검증(validation 넣어보기)
             private Long id;
             private String name;
             private int price;
             private int stockQuantity;
         
             private String author;
             private String isbn;
         }
         ```

     - 상품 등록/조회 컨트롤러 생성

       - ```java
         @Controller
         @RequiredArgsConstructor
         public class ItemController {
             private final ItemService itemService;
         
             @GetMapping("/items/new")
             public String createForm(Model model){
                 model.addAttribute("form", new BookForm());
                 return "items/createItemForm";
             }
         
             @PostMapping("/items/new")
             public String create(BookForm form){
                 Book book = Book.createBook(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
                 itemService.saveItem(book);
                 return "redirect:/items";
             }
         }
         ```

   - 상품 목록

     - 개발완료
     - alt + shift + insert == multi cursor selection

   - 상품 수정

     - 주의
       - 로그인 사용자가 상품수정에 대한 권한 유무를 확인해주는 비즈니스 로직을 서비스단에 추가해야 한다
       - 수정할 객체를 session에 담아두고 사용하는 방법도 있지만 구현이 어렵고, 자주 안 사용하는 방법

   - 변경 감지와 병합(merge)

   - 주목 목록 검색, 취소

8. 정리
