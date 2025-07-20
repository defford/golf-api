package com.golfclub.tournaments;

import com.golfclub.members.MemberModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tournaments")
@CrossOrigin(origins = "*")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping
    public List<TournamentModel> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentModel> getTournamentById(@PathVariable Long id) {
        TournamentModel tournament = tournamentService.getTournamentById(id);
        return tournament != null ? ResponseEntity.ok(tournament) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public TournamentModel createTournament(@RequestBody TournamentModel tournament) {
        return tournamentService.saveTournament(tournament);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentModel> updateTournament(@PathVariable Long id, @RequestBody TournamentModel tournament) {
        TournamentModel existingTournament = tournamentService.getTournamentById(id);
        if (existingTournament == null) {
            return ResponseEntity.notFound().build();
        }
        tournament.setId(id);
        return ResponseEntity.ok(tournamentService.saveTournament(tournament));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        if (tournamentService.getTournamentById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tournamentId}/members/{memberId}")
    public ResponseEntity<TournamentModel> addMemberToTournament(
            @PathVariable Long tournamentId, 
            @PathVariable Long memberId) {
        TournamentModel tournament = tournamentService.addMemberToTournament(tournamentId, memberId);
        return tournament != null ? ResponseEntity.ok(tournament) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{tournamentId}/members/{memberId}")
    public ResponseEntity<TournamentModel> removeMemberFromTournament(
            @PathVariable Long tournamentId, 
            @PathVariable Long memberId) {
        TournamentModel tournament = tournamentService.removeMemberFromTournament(tournamentId, memberId);
        return tournament != null ? ResponseEntity.ok(tournament) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<Set<MemberModel>> getTournamentMembers(@PathVariable Long id) {
        TournamentModel tournament = tournamentService.getTournamentById(id);
        if (tournament == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tournament.getMembers());
    }

    @GetMapping("/search/start-date")
    public List<TournamentModel> searchByStartDate(@RequestParam String startDate) {
        LocalDateTime dateTime = LocalDateTime.parse(startDate + "T00:00:00");
        return tournamentService.findByStartDate(dateTime);
    }

    @GetMapping("/search/location")
    public List<TournamentModel> searchByLocation(@RequestParam String location) {
        return tournamentService.findByLocation(location);
    }
}