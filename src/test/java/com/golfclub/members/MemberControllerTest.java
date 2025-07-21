package com.golfclub.members;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private ObjectMapper objectMapper;
    private MemberModel testMember;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        testMember = new MemberModel();
        testMember.setId(1L);
        testMember.setName("John Doe");
        testMember.setEmail("john.doe@example.com");
        testMember.setPhone("555-0123");
        testMember.setAddress("123 Golf Street");
        testMember.setActive(true);
        testMember.setMembershipType(MemberModel.MembershipType.PREMIUM);
        testMember.setDurationOfMembership("1 year");
        testMember.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getAllMembers_ShouldReturnMemberList() throws Exception {
        List<MemberModel> members = Arrays.asList(testMember);
        when(memberService.getAllMembers()).thenReturn(members);

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }

    @Test
    void getMemberById_WhenMemberExists_ShouldReturnMember() throws Exception {
        when(memberService.getMemberById(1L)).thenReturn(testMember);

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getMemberById_WhenMemberNotExists_ShouldReturnNotFound() throws Exception {
        when(memberService.getMemberById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMember_ShouldReturnCreatedMember() throws Exception {
        when(memberService.saveMember(any(MemberModel.class))).thenReturn(testMember);

        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMember)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void updateMember_WhenMemberExists_ShouldReturnUpdatedMember() throws Exception {
        MemberModel updatedMember = new MemberModel();
        updatedMember.setId(1L);
        updatedMember.setName("Jane Doe");
        updatedMember.setEmail("jane.doe@example.com");

        when(memberService.getMemberById(1L)).thenReturn(testMember);
        when(memberService.saveMember(any(MemberModel.class))).thenReturn(updatedMember);

        mockMvc.perform(put("/api/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMember)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void updateMember_WhenMemberNotExists_ShouldReturnNotFound() throws Exception {
        when(memberService.getMemberById(1L)).thenReturn(null);

        mockMvc.perform(put("/api/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMember)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMember_WhenMemberExists_ShouldReturnNoContent() throws Exception {
        when(memberService.getMemberById(1L)).thenReturn(testMember);

        mockMvc.perform(delete("/api/members/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMember_WhenMemberNotExists_ShouldReturnNotFound() throws Exception {
        when(memberService.getMemberById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/members/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchByName_ShouldReturnFilteredMembers() throws Exception {
        List<MemberModel> members = Arrays.asList(testMember);
        when(memberService.findByName("John")).thenReturn(members);

        mockMvc.perform(get("/api/members/search/name")
                .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void searchByMembershipType_ShouldReturnFilteredMembers() throws Exception {
        List<MemberModel> members = Arrays.asList(testMember);
        when(memberService.findByMembershipType(MemberModel.MembershipType.PREMIUM)).thenReturn(members);

        mockMvc.perform(get("/api/members/search/membership-type")
                .param("membershipType", "PREMIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].membershipType").value("PREMIUM"));
    }

    @Test
    void searchByPhone_ShouldReturnFilteredMembers() throws Exception {
        List<MemberModel> members = Arrays.asList(testMember);
        when(memberService.findByPhone("555-0123")).thenReturn(members);

        mockMvc.perform(get("/api/members/search/phone")
                .param("phone", "555-0123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phone").value("555-0123"));
    }

    @Test
    void searchByTournamentStartDate_ShouldReturnFilteredMembers() throws Exception {
        List<MemberModel> members = Arrays.asList(testMember);
        LocalDateTime testDate = LocalDateTime.of(2024, 1, 15, 0, 0);
        when(memberService.findByTournamentStartDate(any(LocalDateTime.class))).thenReturn(members);

        mockMvc.perform(get("/api/members/search/tournament-date")
                .param("startDate", "2024-01-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }
}