package com.golfclub.members;

import com.golfclub.tournaments.TournamentModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    private MemberModel testMember1;
    private MemberModel testMember2;
    private TournamentModel testTournament;

    @BeforeEach
    void setUp() {
        testMember1 = new MemberModel();
        testMember1.setName("John Doe");
        testMember1.setEmail("john.doe@example.com");
        testMember1.setPhone("555-0123");
        testMember1.setAddress("123 Golf Street");
        testMember1.setActive(true);
        testMember1.setMembershipType(MemberModel.MembershipType.PREMIUM);
        testMember1.setDurationOfMembership("1 year");

        testMember2 = new MemberModel();
        testMember2.setName("Jane Smith");
        testMember2.setEmail("jane.smith@example.com");
        testMember2.setPhone("555-0456");
        testMember2.setAddress("456 Club Avenue");
        testMember2.setActive(true);
        testMember2.setMembershipType(MemberModel.MembershipType.BASIC);
        testMember2.setDurationOfMembership("6 months");

        testTournament = new TournamentModel();
        testTournament.setName("Spring Championship");
        testTournament.setStartDate(LocalDateTime.of(2024, 4, 15, 9, 0));
        testTournament.setLocation("Pine Valley Golf Club");
        testTournament.setDescription("Annual spring tournament");
        
        entityManager.persistAndFlush(testMember1);
        entityManager.persistAndFlush(testMember2);
        entityManager.persistAndFlush(testTournament);
        
        testTournament.addMember(testMember1);
        entityManager.persistAndFlush(testTournament);
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnMatchingMembers() {
        List<MemberModel> result = memberRepository.findByNameContainingIgnoreCase("john");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldBeCaseInsensitive() {
        List<MemberModel> result = memberRepository.findByNameContainingIgnoreCase("JOHN");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnEmptyListWhenNoMatch() {
        List<MemberModel> result = memberRepository.findByNameContainingIgnoreCase("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    void findByMembershipType_ShouldReturnMatchingMembers() {
        List<MemberModel> result = memberRepository.findByMembershipType(MemberModel.MembershipType.PREMIUM);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        assertThat(result.get(0).getMembershipType()).isEqualTo(MemberModel.MembershipType.PREMIUM);
    }

    @Test
    void findByPhone_ShouldReturnMatchingMembers() {
        List<MemberModel> result = memberRepository.findByPhone("555-0123");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        assertThat(result.get(0).getPhone()).isEqualTo("555-0123");
    }

    @Test
    void findByPhone_ShouldReturnEmptyListWhenNoMatch() {
        List<MemberModel> result = memberRepository.findByPhone("999-9999");

        assertThat(result).isEmpty();
    }

    @Test
    void findByTournamentStartDate_ShouldReturnMembersInTournamentOnDate() {
        LocalDateTime testDate = LocalDateTime.of(2024, 4, 15, 0, 0);
        
        List<MemberModel> result = memberRepository.findByTournamentStartDate(testDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
    }

    @Test
    void findByTournamentStartDate_ShouldReturnEmptyListWhenNoTournamentOnDate() {
        LocalDateTime testDate = LocalDateTime.of(2024, 5, 15, 0, 0);
        
        List<MemberModel> result = memberRepository.findByTournamentStartDate(testDate);

        assertThat(result).isEmpty();
    }

    @Test
    void findByTournamentStartDateAfter_ShouldReturnMembersInFutureTournaments() {
        LocalDateTime testDate = LocalDateTime.of(2024, 4, 14, 0, 0);
        
        List<MemberModel> result = memberRepository.findByTournamentStartDateAfter(testDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
    }
}