package com.golfclub.members;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface MemberRepository extends JpaRepository<MemberModel, Long> {
    
    List<MemberModel> findByNameContainingIgnoreCase(String name);
    
    List<MemberModel> findByMembershipType(MemberModel.MembershipType membershipType);
        
    List<MemberModel> findByPhone(String phone);
    
    @Query("SELECT m FROM MemberModel m JOIN m.tournaments t WHERE t.startDate >= :startDate")
    List<MemberModel> findByTournamentStartDateAfter(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT m FROM MemberModel m JOIN m.tournaments t WHERE DATE(t.startDate) = DATE(:startDate)")
    List<MemberModel> findByTournamentStartDate(@Param("startDate") LocalDateTime startDate);
}
