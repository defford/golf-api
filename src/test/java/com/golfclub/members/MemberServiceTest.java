package com.golfclub.members;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private MemberModel testMember;

    @BeforeEach
    void setUp() {
        testMember = new MemberModel();
        testMember.setId(1L);
        testMember.setName("John Doe");
        testMember.setEmail("john.doe@example.com");
        testMember.setPhone("555-0123");
        testMember.setAddress("123 Golf Street");
        testMember.setActive(true);
        testMember.setMembershipType(MemberModel.MembershipType.PREMIUM);
        testMember.setDurationOfMembership("1 year");
        testMember.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getAllMembers_ShouldReturnAllMembers() {
        List<MemberModel> expectedMembers = Arrays.asList(testMember);
        when(memberRepository.findAll()).thenReturn(expectedMembers);

        List<MemberModel> result = memberService.getAllMembers();

        assertThat(result).isEqualTo(expectedMembers);
        verify(memberRepository).findAll();
    }

    @Test
    void getMemberById_WhenMemberExists_ShouldReturnMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

        MemberModel result = memberService.getMemberById(1L);

        assertThat(result).isEqualTo(testMember);
        verify(memberRepository).findById(1L);
    }

    @Test
    void getMemberById_WhenMemberNotExists_ShouldReturnNull() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        MemberModel result = memberService.getMemberById(1L);

        assertThat(result).isNull();
        verify(memberRepository).findById(1L);
    }

    @Test
    void saveMember_ShouldReturnSavedMember() {
        when(memberRepository.save(any(MemberModel.class))).thenReturn(testMember);

        MemberModel result = memberService.saveMember(testMember);

        assertThat(result).isEqualTo(testMember);
        verify(memberRepository).save(testMember);
    }

    @Test
    void deleteMember_ShouldCallRepositoryDelete() {
        memberService.deleteMember(1L);

        verify(memberRepository).deleteById(1L);
    }

    @Test
    void findByName_ShouldReturnMatchingMembers() {
        List<MemberModel> expectedMembers = Arrays.asList(testMember);
        when(memberRepository.findByNameContainingIgnoreCase("John")).thenReturn(expectedMembers);

        List<MemberModel> result = memberService.findByName("John");

        assertThat(result).isEqualTo(expectedMembers);
        verify(memberRepository).findByNameContainingIgnoreCase("John");
    }

    @Test
    void findByMembershipType_ShouldReturnMatchingMembers() {
        List<MemberModel> expectedMembers = Arrays.asList(testMember);
        when(memberRepository.findByMembershipType(MemberModel.MembershipType.PREMIUM))
                .thenReturn(expectedMembers);

        List<MemberModel> result = memberService.findByMembershipType(MemberModel.MembershipType.PREMIUM);

        assertThat(result).isEqualTo(expectedMembers);
        verify(memberRepository).findByMembershipType(MemberModel.MembershipType.PREMIUM);
    }

    @Test
    void findByPhone_ShouldReturnMatchingMembers() {
        List<MemberModel> expectedMembers = Arrays.asList(testMember);
        when(memberRepository.findByPhone("555-0123")).thenReturn(expectedMembers);

        List<MemberModel> result = memberService.findByPhone("555-0123");

        assertThat(result).isEqualTo(expectedMembers);
        verify(memberRepository).findByPhone("555-0123");
    }

    @Test
    void findByTournamentStartDate_ShouldReturnMatchingMembers() {
        LocalDateTime testDate = LocalDateTime.of(2024, 1, 15, 0, 0);
        List<MemberModel> expectedMembers = Arrays.asList(testMember);
        when(memberRepository.findByTournamentStartDate(testDate)).thenReturn(expectedMembers);

        List<MemberModel> result = memberService.findByTournamentStartDate(testDate);

        assertThat(result).isEqualTo(expectedMembers);
        verify(memberRepository).findByTournamentStartDate(testDate);
    }
}