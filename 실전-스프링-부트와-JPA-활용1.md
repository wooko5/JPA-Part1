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

     - 여기까지 했음

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
     
     - `꿀팁`
     
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

     - ```yaml
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
         ```

       - 즉시로딩(EAGER)은 특정 엔티티를 조회할 때, 해당 엔티티와 관련있는 다른 엔티티의 정보도 같이 조회한다

       - 예를 들어, Member와 Order는 1대다 관계이다. 여기서 주인은 Member를 FK로 가지고 있는 Order인데 Order의 데이터를 가져오고 싶을 때 FetchType을 즉시로딩(EAGER)로 하면 Order뿐만 아니라 Member의 정보도 같이 가지고 온다

       - 지연로딩(LAZY)는 특정 엔티티를 조회할 때, 해당 엔티티의 정보만 조회하고 연관된 다른 엔티티의 정보를 가져오지 않는다. 대신 실제로 다른 엔티티에 접근할 때 프록시를 통해 가져온다.

       - `N+1 문제`

         - N + 1 문제는 연관관계가 설정된 엔티티 사이에서 한 엔티티를 조회하였을 때, 조회된 엔티티의 개수(N 개)만큼 연관된 엔티티를 조회하기 위해 추가적인 쿼리가 발생하는 문제
         - 예를 들어, Member와 Order가 연관관계이다. 여기서 Order의 데이터 개수가 100개라고 한다면, Member 정보 1개를 가져올려고 쿼리 1개랑 Order 데이터 100개의 쿼리를 보내서 총 1+100개의 Member 정보를 가져오기 위한 중복 쿼리가 실행된다. 이를 `N+1 문제`라고 한다 
         - 여기까지 했음

     - 컬렉션은 필드에서 초기화하자

     - 테이블명, 칼럼명 생성 전략

3. 애플리케이션 구현 준비

4. 회원 도메인 개발

5. 상품 도메인 개발

6. 주문 도메인 개발

7. 웹 계층 개발

8. 정리

