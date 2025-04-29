package edu.tcu.cs.frogcrewbackend.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrewbackend.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    List<Member> members;

    @BeforeEach
    void setUp(){
        Member mem1 = new Member();
        mem1.setFirstName("Jack");
        mem1.setLastName("Smith");
        mem1.setEmail("jsmith@gmail.com");
        mem1.setPhoneNumber("1234567890");
        mem1.setPassword("password1");
        mem1.setRole("MEMBER");
        mem1.setPositions("Director");

        Member mem2 = new Member();
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

    @AfterEach
    void tearDown(){

    }

    @Test
    void testCreateMemberSuccess(){
        // Given
        Member newMember = new Member();
        newMember.setFirstName("Ben");
        newMember.setLastName("Ten");
        newMember.setEmail("b10@gmail.com");
        newMember.setPhoneNumber("0001112222");
        newMember.setPassword("password");
        newMember.setRole("MEMBER");
        newMember.setPositions("Crew");

        given(this.userRepository.save(newMember)).willReturn(newMember);

        // When
        Member returnedMember = this.userService.createMember(newMember);

        // Then
        assertThat(returnedMember.getFirstName()).isEqualTo(newMember.getFirstName());
        assertThat(returnedMember.getLastName()).isEqualTo(newMember.getLastName());
        assertThat(returnedMember.getEmail()).isEqualTo(newMember.getEmail());
        assertThat(returnedMember.getPhoneNumber()).isEqualTo(newMember.getPhoneNumber());
        assertThat(returnedMember.getRole()).isEqualTo(newMember.getRole());
        assertThat(returnedMember.getPositions()).isEqualTo(newMember.getPositions());
        verify(this.userRepository, times(1)).save(newMember);
    }

    @Test
    void testFindAllMembersSuccess(){
        // Given
        given(this.userRepository.findAll()).willReturn(this.members);

        // When
        List<Member> returnedMembers = this.userService.findAllMembers();

        // Then
        assertThat(returnedMembers.size()).isEqualTo(this.members.size());

        // Verify
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    void testFindMemberByIdSuccess() {
        // Given
        Member member = new Member();
        member.setId(1);
        member.setFirstName("Jack");
        member.setLastName("Smith");
        member.setEmail("jsmith@gmail.com");
        member.setPhoneNumber("1234567890");
        member.setPassword("password");
        member.setRole("MEMBER");
        member.setPositions("Director");

        given(this.userRepository.findById(1)).willReturn(Optional.of(member));

        // When
        Member returnedMember = this.userService.findMemberById(1);

        // Then
        assertThat(returnedMember.getFirstName()).isEqualTo(member.getFirstName());
        assertThat(returnedMember.getLastName()).isEqualTo(member.getLastName());
        assertThat(returnedMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(returnedMember.getPhoneNumber()).isEqualTo(member.getPhoneNumber());
        assertThat(returnedMember.getRole()).isEqualTo(member.getRole());
        assertThat(returnedMember.getPositions()).isEqualTo(member.getPositions());
        verify(this.userRepository, times(1)).findById(1);
    }

    @Test
    void testFindMemberByIdNotFound() {
        // Given
        given(this.userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            Member returnedMember = this.userService.findMemberById(1);
        });

        // Then
        assertThat(thrown)
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find member with Id 1");

        verify(this.userRepository, times(1)).findById(Mockito.any(Integer.class));
    }

//    @Test
//    void testUpdateMemberSuccess(){
//        // Given
//        Member oldMember = new Member();
//        oldMember.setId(1);
//        oldMember.setFirstName("Jane");
//        oldMember.setLastName("Smith");
//        oldMember.setEmail("jane.smith@gmail.com");
//        oldMember.setPhoneNumber("0123456789");
//        oldMember.setPassword("password");
//        oldMember.setRole("MEMBER");
//        oldMember.setPositions("Director");
//
//        Member update = new Member();
//        update.setFirstName("Jenny");
//        update.setLastName("Smith");
//        update.setEmail("jane.smith@gmail.com");
//        update.setPhoneNumber("0123456789");
//        update.setPassword("password");
//        update.setRole("MEMBER");
//        update.setPositions("Director");
//
//        given(this.userRepository.findById(1)).willReturn(Optional.of(oldMember));
//        given(this.userRepository.save(oldMember)).willReturn(oldMember);
//
//        // When
//        Member updatedMember = this.userService.updateMember(1, update);
//
//        // Then
//        assertThat(updatedMember.getId()).isEqualTo(1);
//        assertThat(updatedMember.getFirstName()).isEqualTo(update.getFirstName());
//        verify(this.userRepository, times(1)).findById(1);
//        verify(this.userRepository, times(1)).save(oldMember);
//    }
//
//    @Test
//    void testUpdateMemberNotFound() {
//        // Given
//        Member update = new Member();
//        update.setFirstName("Jane");
//        update.setLastName("Smith");
//        update.setEmail("jane.smith@gmail.com");
//        update.setPhoneNumber("0123456789");
//        update.setPassword("password");
//        update.setRole("MEMBER");
//        update.setPositions("Director");
//
//        given(this.userRepository.findById(1)).willReturn(Optional.empty());
//
//        // When
//        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
//            this.userService.updateMember(1, update);
//        });
//
//        // Then
//        assertThat(thrown)
//            .isInstanceOf(ObjectNotFoundException.class)
//            .hasMessage("Could not find member with Id 1");
//        verify(this.userRepository, times(1)).findById(1);
//    }

    @Test
    void testDeleteMemberSuccess() {
        // Given
        Member member = new Member();
        member.setId(1);
        member.setFirstName("Jane");
        member.setLastName("Smith");
        member.setEmail("jane.smith@gmail.com");
        member.setPhoneNumber("0123456789");
        member.setPassword("password");
        member.setRole("MEMBER");
        member.setPositions("Director");

        given(this.userRepository.findById(1)).willReturn(Optional.of(member));
        doNothing().when(this.userRepository).deleteById(1);

        // When
        this.userService.deleteMember(1);

        // Then
        verify(this.userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteMemberNotFound() {
        // Given
        given(this.userRepository.findById(1)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.deleteMember(1);
        });

        // Then
        assertThat(thrown)
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find member with Id 1");
        verify(this.userRepository, times(1)).findById(1);
    }
}
