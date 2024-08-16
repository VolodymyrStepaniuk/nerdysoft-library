package com.stepaniuk.nerdysoft.member;

import com.stepaniuk.nerdysoft.member.exception.MemberNotFoundByIdException;
import com.stepaniuk.nerdysoft.testspecific.JpaLevelTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JpaLevelTest
@Sql(scripts = {"classpath:sql/members.sql"})
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void shouldSaveMember() {
        // given
        Instant membershipDate = Instant.now();
        Member memberToSave = new Member(null, "John", "Doe", membershipDate);

        // when
        Member savedMember = memberRepository.save(memberToSave);

        // then
        assertNotNull(savedMember);
        assertNotNull(savedMember.getId());
        assertEquals(memberToSave.getName(), savedMember.getName());
        assertEquals(memberToSave.getSurname(), savedMember.getSurname());
        assertEquals(memberToSave.getMembershipDate(), savedMember.getMembershipDate());
    }

    @Test
    void shouldReturnMemberWhenFindById(){
        // when
        Optional<Member> optionalMember = memberRepository.findById(1L);

        // then
        assertTrue(optionalMember.isPresent());
        Member member = optionalMember.get();

        assertNotNull(member);
        assertEquals(1L, member.getId());
        assertEquals("John", member.getName());
        assertEquals("Doe", member.getSurname());
        assertNotNull(member.getMembershipDate());
    }

    @Test
    void shouldUpdateMemberWhenChangingName(){
        var givenMemberId = 1L;
        Member memberToUpdate = memberRepository.findById(givenMemberId).orElseThrow(
                () -> new MemberNotFoundByIdException(givenMemberId)
        );

        // when
        memberToUpdate.setName("Jane");
        Member savedMember = memberRepository.save(memberToUpdate);
        assertNotNull(savedMember);
        assertEquals(memberToUpdate.getId(), savedMember.getId());
        assertEquals("Jane", savedMember.getName());
    }

    @Test
    void shouldUpdateMemberWhenChangingAllFields(){
        var givenMemberId = 1L;
        Member memberToUpdate = memberRepository.findById(givenMemberId).orElseThrow(
                () -> new MemberNotFoundByIdException(givenMemberId)
        );

        // when
        memberToUpdate.setName("Jane");
        memberToUpdate.setSurname("Smith");
        memberToUpdate.setMembershipDate(Instant.now());

        Member savedMember = memberRepository.save(memberToUpdate);

        // then
        assertNotNull(savedMember);
        assertEquals(memberToUpdate.getId(), savedMember.getId());
        assertEquals("Jane", savedMember.getName());
        assertEquals("Smith", savedMember.getSurname());
        assertNotNull(savedMember.getMembershipDate());
    }

    @Test
    void shouldDeleteMemberWhenDeletingByExistingMember(){
        // given
        var givenMemberId = 1L;
        Member memberToDelete = memberRepository.findById(givenMemberId).orElseThrow(
                () -> new MemberNotFoundByIdException(givenMemberId)
        );

        // when
        memberRepository.delete(memberToDelete);

        // then
        assertTrue(memberRepository.findById(givenMemberId).isEmpty());
    }

    @Test
    void shouldDeleteMemberByIdWhenDeletingByExistingId(){
        // when
        memberRepository.deleteById(1L);

        // then
        assertTrue(memberRepository.findById(1L).isEmpty());
    }

    @Test
    void shouldReturnTrueWhenMemberExists(){
        // when
        boolean exists = memberRepository.existsById(1L);

        // then
        assertTrue(exists);
    }

    @Test
    void shouldReturnNonEmptyListWhenFindAll() {
        // when
        var members = memberRepository.findAll();

        // then
        assertNotNull(members);
        assertFalse(members.isEmpty());
    }
}
