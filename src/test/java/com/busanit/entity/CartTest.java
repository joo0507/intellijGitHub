package com.busanit.entity;


import com.busanit.repository.CartRepository;
import com.busanit.repository.MemberRepository;
import domain.MemberFormDto;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.PushBuilder;
import javax.validation.Valid;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations ="classpath:application-test.properties")

class CartTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder ;
    @Autowired
    private CartRepository cartRepository;

    @PersistenceContext
    EntityManager em;
    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto,passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);


        em.flush(); //강제로 데이터베이스에 반영
        em.clear(); //영속성 컨텍스트 비워줌

        // 저장된 장바구니 엔티티조회

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        //savedCart 의 id 값과 member 엔티티의 id 값을 비교
        assertEquals(savedCart.getMember().getId(),member.getId());



    }
}