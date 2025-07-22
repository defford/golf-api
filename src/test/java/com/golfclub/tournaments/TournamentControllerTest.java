package com.golfclub.tournaments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.golfclub.members.MemberModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TournamentController.class)
public class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentService tournamentService;

    private ObjectMapper objectMapper;
    private TournamentModel testTournament;
    private MemberModel testMember;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
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
    void getAllTournaments_ShouldReturnTournamentList() throws Exception {
        List<TournamentModel> tournaments = Arrays.asList(testTournament);
        when(tournamentService.getAllTournaments()).thenReturn(tournaments);

        mockMvc.perform(get("/api/tournaments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Spring Championship"))
                .andExpect(jsonPath("$[0].location").value("Pine Valley Golf Club"));
    }

    @Test
    void getTournamentById_WhenTournamentExists_ShouldReturnTournament() throws Exception {
        when(tournamentService.getTournamentById(1L)).thenReturn(testTournament);

        mockMvc.perform(get("/api/tournaments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Spring Championship"))
                .andExpect(jsonPath("$.location").value("Pine Valley Golf Club"));
    }

    @Test
    void getTournamentById_WhenTournamentNotExists_ShouldReturnNotFound() throws Exception {
        when(tournamentService.getTournamentById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/tournaments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTournament_ShouldReturnCreatedTournament() throws Exception {
        when(tournamentService.saveTournament(any(TournamentModel.class))).thenReturn(testTournament);

        mockMvc.perform(post("/api/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTournament)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Spring Championship"));
    }

    @Test
    void updateTournament_WhenTournamentExists_ShouldReturnUpdatedTournament() throws Exception {
        TournamentModel updatedTournament = new TournamentModel();
        updatedTournament.setId(1L);
        updatedTournament.setName("Summer Championship");
        updatedTournament.setStartDate(LocalDateTime.of(2024, 6, 15, 10, 0));
        updatedTournament.setLocation("Oak Hill Golf Club");

        when(tournamentService.getTournamentById(1L)).thenReturn(testTournament);
        when(tournamentService.saveTournament(any(TournamentModel.class))).thenReturn(updatedTournament);

        mockMvc.perform(put("/api/tournaments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTournament)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Summer Championship"));
    }

    @Test
    void updateTournament_WhenTournamentNotExists_ShouldReturnNotFound() throws Exception {
        when(tournamentService.getTournamentById(1L)).thenReturn(null);

        mockMvc.perform(put("/api/tournaments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTournament)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTournament_WhenTournamentExists_ShouldReturnNoContent() throws Exception {
        when(tournamentService.getTournamentById(1L)).thenReturn(testTournament);

        mockMvc.perform(delete("/api/tournaments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTournament_WhenTournamentNotExists_ShouldReturnNotFound() throws Exception {
        when(tournamentService.getTournamentById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/tournaments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addMemberToTournament_WhenBothExist_ShouldReturnUpdatedTournament() throws Exception {
        testTournament.getMembers().add(testMember);
        when(tournamentService.addMemberToTournament(1L, 1L)).thenReturn(testTournament);

        mockMvc.perform(post("/api/tournaments/1/members/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Spring Championship"));
    }

    @Test
    void addMemberToTournament_WhenTournamentOrMemberNotExists_ShouldReturnNotFound() throws Exception {
        when(tournamentService.addMemberToTournament(1L, 1L)).thenReturn(null);

        mockMvc.perform(post("/api/tournaments/1/members/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeMemberFromTournament_WhenBothExist_ShouldReturnUpdatedTournament() throws Exception {
        when(tournamentService.removeMemberFromTournament(1L, 1L)).thenReturn(testTournament);

        mockMvc.perform(delete("/api/tournaments/1/members/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Spring Championship"));
    }

    @Test
    void removeMemberFromTournament_WhenTournamentOrMemberNotExists_ShouldReturnNotFound() throws Exception {
        when(tournamentService.removeMemberFromTournament(1L, 1L)).thenReturn(null);

        mockMvc.perform(delete("/api/tournaments/1/members/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTournamentMembers_WhenTournamentExists_ShouldReturnMembers() throws Exception {
        Set<MemberModel> members = new HashSet<>();
        members.add(testMember);
        testTournament.setMembers(members);
        when(tournamentService.getTournamentById(1L)).thenReturn(testTournament);

        mockMvc.perform(get("/api/tournaments/1/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getTournamentMembers_WhenTournamentNotExists_ShouldReturnNotFound() throws Exception {
        when(tournamentService.getTournamentById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/tournaments/1/members"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchByStartDate_ShouldReturnFilteredTournaments() throws Exception {
        List<TournamentModel> tournaments = Arrays.asList(testTournament);
        when(tournamentService.findByStartDate(any(LocalDateTime.class))).thenReturn(tournaments);

        mockMvc.perform(get("/api/tournaments/search/start-date")
                .param("startDate", "2024-04-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Spring Championship"));
    }

    @Test
    void searchByLocation_ShouldReturnFilteredTournaments() throws Exception {
        List<TournamentModel> tournaments = Arrays.asList(testTournament);
        when(tournamentService.findByLocation("Pine Valley")).thenReturn(tournaments);

        mockMvc.perform(get("/api/tournaments/search/location")
                .param("location", "Pine Valley"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value("Pine Valley Golf Club"));
    }
}