package edu.tcu.cs.frogcrewbackend.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrewbackend.member.converter.UserDTOToUserConverter;
import edu.tcu.cs.frogcrewbackend.member.converter.UserToUserDTOConverter;
import edu.tcu.cs.frogcrewbackend.member.dto.UserDTO;
import edu.tcu.cs.frogcrewbackend.system.StatusCode;
import edu.tcu.cs.frogcrewbackend.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    @MockitoBean
    UserToUserDTOConverter userToUserDTOConverter;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    ArrayList<Member> members;

    @BeforeEach
    void setUp(){
        Member mem1 = new Member();
        mem1.setId(1);
        mem1.setId(1);
        mem1.setFirstName("John");
        mem1.setLastName("Smith");
        mem1.setEmail("john.smith@gmail.com");
        mem1.setPhoneNumber("1234567890");
        mem1.setPassword("password");
        mem1.setRole("ADMIN");
        mem1.setPositions("DIRECTOR");

        Member mem2 = new Member();
        mem2.setId(2);
        mem2.setFirstName("Jane");
        mem2.setLastName("Smith");
        mem2.setEmail("jane.smith@gmail.com");
        mem2.setPhoneNumber("0123456789");
        mem2.setPassword("password2");
        mem2.setRole("MEMBER");
        mem2.setPositions("Videographer Planner");

        this.members = new ArrayList<>();
        this.members.add(mem1);
        this.members.add(mem2);
    }

    @Test
    void testCreateMemberSuccess() throws Exception {
        Member member = new Member();
        member.setId(1);
        member.setFirstName("John");
        member.setLastName("Smith");
        member.setEmail("john.smith@gmail.com");
        member.setPhoneNumber("1234567890");
        member.setPassword("password");
        member.setRole("ADMIN");
        member.setPositions("DIRECTOR");

        UserDTO memberDTO = new UserDTO(1, "John", "Smith", "john.smith@gmail.com", "1234567890", "ADMIN", "DIRECTOR");

        String json = this.objectMapper.writeValueAsString(member);

        // Given
        given(this.userService.createMember(Mockito.any(Member.class))).willReturn(member);
        given(this.userToUserDTOConverter.convert(Mockito.any(Member.class))).willReturn(memberDTO);

        // When and Then
        this.mockMvc.perform(
                post(this.baseUrl + "/crewMember")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Member created"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.lastName").value("Smith"));
    }

    @Test
    void testFindAllMembersSuccess() throws Exception {
        // Given
        given(this.userService.findAllMembers()).willReturn(this.members);
        given(this.userToUserDTOConverter.convert(this.members.get(0)))
                .willReturn(new UserDTO(1, "John", "Smith", "john.smith@gmail.com", "1234567890", "ADMIN", "DIRECTOR"));

        given(this.userToUserDTOConverter.convert(this.members.get(1)))
                .willReturn(new UserDTO(2, "Jane", "Smith", "jane.smith@gmail.com", "0123456789", "MEMBER", "Videographer Planner"));

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/crewMember")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found all members"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].firstName").value("John"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].firstName").value("Jane"));
    }

    @Test
    void testFindMemberByIdSuccess() throws Exception {
        // Given
        given(this.userService.findMemberById(1)).willReturn(this.members.get(0));
        given(this.userToUserDTOConverter.convert(this.members.get(0)))
                .willReturn(new UserDTO(1, "John", "Smith", "john.smith@gmail.com", "1234567890", "ADMIN", "DIRECTOR"));

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/crewMember/1")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found member with Id: 1"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firstName").value("John"));
    }

    @Test
    void testFindMemberByIdNotFound() throws Exception {
        // Given
        given(this.userService.findMemberById(3)).willThrow(new ObjectNotFoundException("member", 3));
        given(this.userToUserDTOConverter.convert(this.members.get(0)))
                .willReturn(new UserDTO(1, "John", "Smith", "john.smith@gmail.com", "1234567890", "ADMIN", "DIRECTOR"));

        given(this.userToUserDTOConverter.convert(this.members.get(1)))
                .willReturn(new UserDTO(2, "Jane", "Smith", "jane.smith@gmail.com", "0123456789", "MEMBER", "Videographer Planner"));

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/crewMember/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find member with Id 3"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

//    @Test
//    void testUpdateMemberSuccess() throws Exception {
//        UserDTO userDTO = new UserDTO(3, "jane", "smith", "js@gmail.com", "1234567890", "MEMBER", "Producer");
//
//        Member update = new Member();
//        update.setId(3);
//        update.setFirstName("Jane");
//        update.setLastName("Nguyen");
//        update.setEmail("js@gmail.com");
//        update.setPhoneNumber("1234567890");
//        update.setPassword("password");
//        update.setRole("MEMBER");
//        update.setPositions("Producer");
//
//        String json = this.objectMapper.writeValueAsString(userDTO);
//
//        // Given
//        given(this.userService.updateMember(eq(3), Mockito.any(Member.class))).willReturn(update);
//
//        // When and Then
//        this.mockMvc.perform(put(this.baseUrl + "/crewMember/3")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag").value(true))
//                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
//                .andExpect(jsonPath("$.message").value("Member updated with Id: 3"))
//                .andExpect(jsonPath("$.data.id").value(3))
//                .andExpect(jsonPath("$.data.lastName").value("Nguyen"));
//    }
//
//    @Test
//    void testUpdateMemberNotFound() throws Exception {
//        // Given
//        given(this.userService.updateMember(eq(3), Mockito.any(Member.class))).willThrow(new ObjectNotFoundException("member", 3));
//
//        UserDTO userDTO = new UserDTO(3, "jane", "smith", "js@gmail.com", "1234567890", "MEMBER", "Producer");
//
//        String json = this.objectMapper.writeValueAsString(userDTO);
//
//        // When and then
//        this.mockMvc.perform(put(this.baseUrl + "/crewMember/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag").value(false))
//                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
//                .andExpect(jsonPath("$.message").value("Could not find member with Id 3"))
//                .andExpect(jsonPath("$.data").isEmpty());
//    }

    @Test
    void testDeleteMemberSuccess() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        doNothing().when(this.userService).deleteMember(2);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/crewMember/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Member deleted with Id: 2"));
    }

    @Test
    void testDeleteUserErrorWithNonExistentId() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        doThrow(new ObjectNotFoundException("member", 3)).when(this.userService).deleteMember(3);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/crewMember/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find member with Id 3"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
