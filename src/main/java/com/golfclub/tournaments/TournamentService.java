package com.golfclub.tournaments;

import com.golfclub.members.MemberModel;
import com.golfclub.members.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private MemberService memberService;

    public List<TournamentModel> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public TournamentModel getTournamentById(Long id) {
        return tournamentRepository.findById(id).orElse(null);
    }

    public TournamentModel saveTournament(TournamentModel tournament) {
        return tournamentRepository.save(tournament);
    }

    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

    public TournamentModel addMemberToTournament(Long tournamentId, Long memberId) {
        TournamentModel tournament = getTournamentById(tournamentId);
        MemberModel member = memberService.getMemberById(memberId);
        
        if (tournament != null && member != null) {
            tournament.addMember(member);
            return saveTournament(tournament);
        }
        return null;
    }

    public TournamentModel removeMemberFromTournament(Long tournamentId, Long memberId) {
        TournamentModel tournament = getTournamentById(tournamentId);
        MemberModel member = memberService.getMemberById(memberId);
        
        if (tournament != null && member != null) {
            tournament.removeMember(member);
            return saveTournament(tournament);
        }
        return null;
    }

    public List<TournamentModel> findByStartDate(LocalDateTime startDate) {
        return tournamentRepository.findByStartDate(startDate);
    }

    public List<TournamentModel> findByLocation(String location) {
        return tournamentRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<TournamentModel> findTournamentsByMemberId(Long memberId) {
        return tournamentRepository.findTournamentsByMemberId(memberId);
    }
}