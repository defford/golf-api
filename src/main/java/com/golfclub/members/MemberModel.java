package com.golfclub.members;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import com.golfclub.tournaments.TournamentModel;

@Entity
@Table(name = "members")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "tournaments")
public class MemberModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "is_active")
    private boolean isActive;
    
    @Column(unique = true)
    private String email;
    
    private String phone;
    private String address;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "membership_type")
    private MembershipType membershipType;
    
    @Column(name = "duration_of_membership")
    private String durationOfMembership;
    
    @ManyToMany(mappedBy = "members")
    @JsonIgnoreProperties("members")
    private Set<TournamentModel> tournaments = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum MembershipType {
        BASIC, PREMIUM, VIP
    }

}

