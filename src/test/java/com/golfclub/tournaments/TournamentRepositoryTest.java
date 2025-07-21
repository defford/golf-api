package com.golfclub.tournaments;

import com.golfclub.members.MemberModel;
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
public class TournamentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TournamentRepository tournamentRepository;

    private TournamentModel testTournament1;
    private TournamentModel testTournament2;
    private MemberModel testMember;

    @BeforeEach
    void setUp() {
        testMember = new MemberModel();
        testMember.setName("John Doe");
        testMember.setEmail("john.doe@example.com");
        testMember.setPhone("555-0123");
        testMember.setMembershipType(MemberModel.MembershipType.PREMIUM);

        testTournament1 = new TournamentModel();
        testTournament1.setName("Spring Championship");
        testTournament1.setStartDate(LocalDateTime.of(2024, 4, 15, 9, 0));
        testTournament1.setLocation("Pine Valley Golf Club");
        testTournament1.setDescription("Annual spring tournament");

        testTournament2 = new TournamentModel();
        testTournament2.setName("Summer Open");
        testTournament2.setStartDate(LocalDateTime.of(2024, 7, 20, 8, 0));
        testTournament2.setLocation("Oak Hill Golf Course");
        testTournament2.setDescription("Summer championship");

        entityManager.persistAndFlush(testMember);
        entityManager.persistAndFlush(testTournament1);
        entityManager.persistAndFlush(testTournament2);
        
        testTournament1.addMember(testMember);
        entityManager.persistAndFlush(testTournament1);
    }

    @Test
    void findByStartDate_ShouldReturnTournamentsOnSpecificDate() {
        LocalDateTime testDate = LocalDateTime.of(2024, 4, 15, 0, 0);
        
        List<TournamentModel> result = tournamentRepository.findByStartDate(testDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Spring Championship");
    }

    @Test
    void findByStartDate_ShouldReturnEmptyListWhenNoTournamentOnDate() {
        LocalDateTime testDate = LocalDateTime.of(2024, 5, 15, 0, 0);
        
        List<TournamentModel> result = tournamentRepository.findByStartDate(testDate);

        assertThat(result).isEmpty();
    }

    @Test
    void findByStartDateAfter_ShouldReturnFutureTournaments() {
        LocalDateTime testDate = LocalDateTime.of(2024, 4, 14, 0, 0);
        
        List<TournamentModel> result = tournamentRepository.findByStartDateAfter(testDate);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(TournamentModel::getName)
                .containsExactlyInAnyOrder("Spring Championship", "Summer Open");
    }

    @Test
    void findByStartDateAfter_ShouldReturnEmptyListWhenAllTournamentsArePast() {
        LocalDateTime testDate = LocalDateTime.of(2024, 12, 31, 0, 0);
        
        List<TournamentModel> result = tournamentRepository.findByStartDateAfter(testDate);

        assertThat(result).isEmpty();
    }

    @Test
    void findByLocationContainingIgnoreCase_ShouldReturnMatchingTournaments() {
        List<TournamentModel> result = tournamentRepository.findByLocationContainingIgnoreCase("pine");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Spring Championship");
        assertThat(result.get(0).getLocation()).isEqualTo("Pine Valley Golf Club");
    }

    @Test
    void findByLocationContainingIgnoreCase_ShouldBeCaseInsensitive() {
        List<TournamentModel> result = tournamentRepository.findByLocationContainingIgnoreCase("PINE");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Spring Championship");
    }

    @Test
    void findByLocationContainingIgnoreCase_ShouldReturnEmptyListWhenNoMatch() {
        List<TournamentModel> result = tournamentRepository.findByLocationContainingIgnoreCase("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    void findTournamentsByMemberId_ShouldReturnTournamentsForMember() {
        List<TournamentModel> result = tournamentRepository.findTournamentsByMemberId(testMember.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Spring Championship");
    }

    @Test
    void findTournamentsByMemberId_ShouldReturnEmptyListForNonExistentMember() {
        List<TournamentModel> result = tournamentRepository.findTournamentsByMemberId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findTournamentsByMemberId_ShouldReturnEmptyListForMemberWithNoTournaments() {
        MemberModel anotherMember = new MemberModel();
        anotherMember.setName("Jane Smith");
        anotherMember.setEmail("jane.smith@example.com");
        anotherMember.setMembershipType(MemberModel.MembershipType.BASIC);
        entityManager.persistAndFlush(anotherMember);

        List<TournamentModel> result = tournamentRepository.findTournamentsByMemberId(anotherMember.getId());

        assertThat(result).isEmpty();
    }
}