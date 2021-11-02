package cmpg.photoshare.service;

import cmpg.photoshare.entity.Member;
import cmpg.photoshare.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Iterable<Member> getAllMembers(){
        return memberRepository.findAll();
    }

    public Member addMember(Member member){
        return memberRepository.save(member);
    }

    public void findByEmail(String email){
        memberRepository.findByEmail(email);
    }

    public List<Member> login(String email, String hashedPass) {
        return memberRepository.findByEmailAndHashedPass(email, hashedPass);
    }

    public Member getMember(String email){return memberRepository.findByEmail(email);}

    public void updateMember(Member member){
        memberRepository.updateMember(member.getName(),member.getSurname(), member.getEmail());
    }

}
