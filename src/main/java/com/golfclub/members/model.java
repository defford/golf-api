package com.golfclub.members;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class model {
    private String name;
    private boolean isActive;
    private String email;
    private String phone;
    private String address; 
    private String createdAt;
    private String durationOfMembership;

}

