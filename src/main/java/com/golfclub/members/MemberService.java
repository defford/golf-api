package com.golfclub.members;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<MemberModel> getAllMembers() {
        return memberRepository.findAll();
    }

    public MemberModel getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public MemberModel saveMember(MemberModel member) {
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public List<MemberModel> findByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }

    public List<MemberModel> findByMembershipType(MemberModel.MembershipType membershipType) {
        return memberRepository.findByMembershipType(membershipType);
    }

    public List<MemberModel> findByPhone(String phone) {
        return memberRepository.findByPhone(phone);
    }

    public List<MemberModel> findByTournamentStartDate(LocalDateTime startDate) {
        return memberRepository.findByTournamentStartDate(startDate);
    }
}