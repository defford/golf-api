package com.golfclub.tournaments;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import com.golfclub.members.MemberModel;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "members")
public class TournamentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "Tournament name is required")
    @Size(min = 3, max = 100, message = "Tournament name must be between 3 and 100 characters")
    private String name;
    
    @Column(name = "start_date")
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @NotBlank(message = "Location is required")
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @Column(name = "entry_fee")
    @DecimalMin(value = "0.0", inclusive = true, message = "Entry fee must be non-negative")
    private Double entryFee;
    
    @Column(name = "cash_prize_amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Cash prize amount must be non-negative")
    private Double cashPrizeAmount;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "tournament_members",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @JsonIgnoreProperties("tournaments")
    private Set<MemberModel> members = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public void addMember(MemberModel member) {
        members.add(member);
        member.getTournaments().add(this);
    }
    
    public void removeMember(MemberModel member) {
        members.remove(member);
        member.getTournaments().remove(this);
    }
    
    
}
