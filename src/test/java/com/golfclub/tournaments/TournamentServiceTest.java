package com.golfclub.tournaments;

import com.golfclub.members.MemberModel;
import com.golfclub.members.MemberService;
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
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private TournamentService tournamentService;

    private TournamentModel testTournament;
    private MemberModel testMember;

    @BeforeEach
    void setUp() {
        testMember = new MemberModel();
        testMember.setId(1L);
        testMember.setName("John Doe");
        testMember.setEmail("john.doe@example.com");

        testTournament = new TournamentModel();
        testTournament.setId(1L);
        testTournament.setName("Spring Championship");
        testTournament.setStartDate(LocalDateTime.of(2024, 4, 15, 9, 0));
        testTournament.setLocation("Pine Valley Golf Club");
        testTournament.setDescription("Annual spring tournament");
        testTournament.setCreatedAt(LocalDateTime.now());
        testTournament.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getAllTournaments_ShouldReturnAllTournaments() {
        List<TournamentModel> expectedTournaments = Arrays.asList(testTournament);
        when(tournamentRepository.findAll()).thenReturn(expectedTournaments);

        List<TournamentModel> result = tournamentService.getAllTournaments();

        assertThat(result).isEqualTo(expectedTournaments);
        verify(tournamentRepository).findAll();
    }

    @Test
    void getTournamentById_WhenTournamentExists_ShouldReturnTournament() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournament));

        TournamentModel result = tournamentService.getTournamentById(1L);

        assertThat(result).isEqualTo(testTournament);
        verify(tournamentRepository).findById(1L);
    }

    @Test
    void getTournamentById_WhenTournamentNotExists_ShouldReturnNull() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

        TournamentModel result = tournamentService.getTournamentById(1L);

        assertThat(result).isNull();
        verify(tournamentRepository).findById(1L);
    }

    @Test
    void saveTournament_ShouldReturnSavedTournament() {
        when(tournamentRepository.save(any(TournamentModel.class))).thenReturn(testTournament);

        TournamentModel result = tournamentService.saveTournament(testTournament);

        assertThat(result).isEqualTo(testTournament);
        verify(tournamentRepository).save(testTournament);
    }

    @Test
    void deleteTournament_ShouldCallRepositoryDelete() {
        tournamentService.deleteTournament(1L);

        verify(tournamentRepository).deleteById(1L);
    }

    @Test
    void addMemberToTournament_WhenBothExist_ShouldAddMemberAndSaveTournament() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournament));
        when(memberService.getMemberById(1L)).thenReturn(testMember);
        when(tournamentRepository.save(any(TournamentModel.class))).thenReturn(testTournament);

        TournamentModel result = tournamentService.addMemberToTournament(1L, 1L);

        assertThat(result).isEqualTo(testTournament);
        assertThat(testTournament.getMembers()).contains(testMember);
        verify(tournamentRepository).findById(1L);
        verify(memberService).getMemberById(1L);
        verify(tournamentRepository).save(testTournament);
    }

    @Test
    void addMemberToTournament_WhenTournamentNotExists_ShouldReturnNull() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());
        when(memberService.getMemberById(1L)).thenReturn(testMember);

        TournamentModel result = tournamentService.addMemberToTournament(1L, 1L);

        assertThat(result).isNull();
        verify(tournamentRepository).findById(1L);
        verify(memberService).getMemberById(1L);
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    void addMemberToTournament_WhenMemberNotExists_ShouldReturnNull() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournament));
        when(memberService.getMemberById(1L)).thenReturn(null);

        TournamentModel result = tournamentService.addMemberToTournament(1L, 1L);

        assertThat(result).isNull();
        verify(tournamentRepository).findById(1L);
        verify(memberService).getMemberById(1L);
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    void removeMemberFromTournament_WhenBothExist_ShouldRemoveMemberAndSaveTournament() {
        testTournament.getMembers().add(testMember);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournament));
        when(memberService.getMemberById(1L)).thenReturn(testMember);
        when(tournamentRepository.save(any(TournamentModel.class))).thenReturn(testTournament);

        TournamentModel result = tournamentService.removeMemberFromTournament(1L, 1L);

        assertThat(result).isEqualTo(testTournament);
        assertThat(testTournament.getMembers()).doesNotContain(testMember);
        verify(tournamentRepository).findById(1L);
        verify(memberService).getMemberById(1L);
        verify(tournamentRepository).save(testTournament);
    }

    @Test
    void removeMemberFromTournament_WhenTournamentNotExists_ShouldReturnNull() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());
        when(memberService.getMemberById(1L)).thenReturn(testMember);

        TournamentModel result = tournamentService.removeMemberFromTournament(1L, 1L);

        assertThat(result).isNull();
        verify(tournamentRepository).findById(1L);
        verify(memberService).getMemberById(1L);
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    void findByStartDate_ShouldReturnMatchingTournaments() {
        LocalDateTime testDate = LocalDateTime.of(2024, 4, 15, 0, 0);
        List<TournamentModel> expectedTournaments = Arrays.asList(testTournament);
        when(tournamentRepository.findByStartDate(testDate)).thenReturn(expectedTournaments);

        List<TournamentModel> result = tournamentService.findByStartDate(testDate);

        assertThat(result).isEqualTo(expectedTournaments);
        verify(tournamentRepository).findByStartDate(testDate);
    }

    @Test
    void findByLocation_ShouldReturnMatchingTournaments() {
        List<TournamentModel> expectedTournaments = Arrays.asList(testTournament);
        when(tournamentRepository.findByLocationContainingIgnoreCase("Pine Valley"))
                .thenReturn(expectedTournaments);

        List<TournamentModel> result = tournamentService.findByLocation("Pine Valley");

        assertThat(result).isEqualTo(expectedTournaments);
        verify(tournamentRepository).findByLocationContainingIgnoreCase("Pine Valley");
    }

    @Test
    void findTournamentsByMemberId_ShouldReturnMatchingTournaments() {
        List<TournamentModel> expectedTournaments = Arrays.asList(testTournament);
        when(tournamentRepository.findTournamentsByMemberId(1L)).thenReturn(expectedTournaments);

        List<TournamentModel> result = tournamentService.findTournamentsByMemberId(1L);

        assertThat(result).isEqualTo(expectedTournaments);
        verify(tournamentRepository).findTournamentsByMemberId(1L);
    }
}