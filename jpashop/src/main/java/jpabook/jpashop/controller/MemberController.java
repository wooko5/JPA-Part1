package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

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
