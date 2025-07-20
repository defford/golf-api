package com.golfclub.tournaments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface TournamentRepository extends JpaRepository<TournamentModel, Long> {
    
    @Query("SELECT t FROM TournamentModel t WHERE DATE(t.startDate) = DATE(:startDate)")
    List<TournamentModel> findByStartDate(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT t FROM TournamentModel t WHERE t.startDate >= :startDate")
    List<TournamentModel> findByStartDateAfter(@Param("startDate") LocalDateTime startDate);
    
    List<TournamentModel> findByLocationContainingIgnoreCase(String location);
    
    @Query("SELECT t FROM TournamentModel t JOIN t.members m WHERE m.id = :memberId")
    List<TournamentModel> findTournamentsByMemberId(@Param("memberId") Long memberId);
}