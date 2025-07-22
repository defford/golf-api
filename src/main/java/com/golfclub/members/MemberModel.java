package com.golfclub.members;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Column(name = "is_active")
    private boolean isActive;
    
    @Column(unique = true)
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;
    
    @Pattern(regexp = "^\\+?[0-9\\-\\s\\(\\)]+$", message = "Please provide a valid phone number")
    private String phone;
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "duration_of_membership")
    @Size(max = 50, message = "Duration of membership must not exceed 50 characters")
    private String durationOfMembership;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "membership_type")
    private MembershipType membershipType;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
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

