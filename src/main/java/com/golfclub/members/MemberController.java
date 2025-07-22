package com.golfclub.members;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "https://golfclub.com"})
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public List<MemberModel> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberModel> getMemberById(@PathVariable Long id) {
        MemberModel member = memberService.getMemberById(id);
        return member != null ? ResponseEntity.ok(member) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public MemberModel createMember(@Valid @RequestBody MemberModel member) {
        return memberService.saveMember(member);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberModel> updateMember(@PathVariable Long id, @Valid @RequestBody MemberModel member) {
        MemberModel existingMember = memberService.getMemberById(id);
        if (existingMember == null) {
            return ResponseEntity.notFound().build();
        }
        member.setId(id);
        return ResponseEntity.ok(memberService.saveMember(member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        if (memberService.getMemberById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/name")
    public List<MemberModel> searchByName(@RequestParam String name) {
        return memberService.findByName(name);
    }

    @GetMapping("/search/membership-type")
    public List<MemberModel> searchByMembershipType(@RequestParam MemberModel.MembershipType membershipType) {
        return memberService.findByMembershipType(membershipType);
    }

    @GetMapping("/search/phone")
    public List<MemberModel> searchByPhone(@RequestParam String phone) {
        return memberService.findByPhone(phone);
    }

    @GetMapping("/search/tournament-date")
    public List<MemberModel> searchByTournamentStartDate(@RequestParam String startDate) {
        LocalDateTime dateTime = LocalDateTime.parse(startDate + "T00:00:00");
        return memberService.findByTournamentStartDate(dateTime);
    }
}