package com.stepaniuk.nerdysoft.member;

import com.stepaniuk.nerdysoft.book.borrowed.BorrowedBookRepository;
import com.stepaniuk.nerdysoft.member.exception.MemberNotFoundByIdException;
import com.stepaniuk.nerdysoft.member.payload.MemberCreateRequest;
import com.stepaniuk.nerdysoft.member.payload.MemberUpdateRequest;
import com.stepaniuk.nerdysoft.testspecific.ServiceLevelUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ServiceLevelUnitTest
@ContextConfiguration(classes = {MemberService.class, MemberMapperImpl.class})
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private BorrowedBookRepository borrowedBookRepository;

    @Test
    void shouldReturnMemberResponseWhenCreatingMember(){
        // given
        var request = new MemberCreateRequest("John", "Doe");

        when(memberRepository.save(any())).thenAnswer(answer(getFakeSaveAnswer(1L)));

        // when
        var memberResponse = memberService.createMember(request);

        // then
        assertNotNull(memberResponse);
        assertEquals(request.getName(), memberResponse.getName());
        assertEquals(request.getSurname(), memberResponse.getSurname());
        assertTrue(memberResponse.hasLinks());
        assertTrue(memberResponse.getLinks().hasLink("self"));
        assertTrue(memberResponse.getLinks().hasLink("update"));
        assertTrue(memberResponse.getLinks().hasLink("delete"));
    }

    @Test
    void shouldReturnMemberResponseWhenGettingMember(){
        // given
        var member = new Member(1L, "John", "Doe", Instant.now());
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // when
        var memberResponse = memberService.getMember(1L);

        // then
        assertNotNull(memberResponse);
        assertEquals(member.getName(), memberResponse.getName());
        assertEquals(member.getSurname(), memberResponse.getSurname());
        assertTrue(memberResponse.hasLinks());
        assertTrue(memberResponse.getLinks().hasLink("self"));
        assertTrue(memberResponse.getLinks().hasLink("update"));
        assertTrue(memberResponse.getLinks().hasLink("delete"));
    }

    @Test
    void shouldThrowMemberNotFoundByIdExceptionWhenGettingNonExistingMember(){
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundByIdException.class, () -> memberService.getMember(1L));
    }

    @Test
    void shouldUpdateAndReturnMemberResponseWhenChangingName() {
        // given
        var member = new Member(2L, "John", "Doe", Instant.now());
        var request = new MemberUpdateRequest("Jane", null);
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any())).thenAnswer(answer(getFakeSaveAnswer(2L)));

        // when
        var memberResponse = memberService.updateMember(2L, request);

        // then
        assertNotNull(memberResponse);
        assertEquals(request.getName(), memberResponse.getName());
        assertEquals(member.getSurname(), memberResponse.getSurname());
        assertTrue(memberResponse.hasLinks());
        assertTrue(memberResponse.getLinks().hasLink("self"));
        assertTrue(memberResponse.getLinks().hasLink("update"));
        assertTrue(memberResponse.getLinks().hasLink("delete"));
    }

    @Test
    void shouldThrowMemberNotFoundByIdExceptionWhenUpdatingNonExistingMember(){
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundByIdException.class, () -> memberService.updateMember(1L, new MemberUpdateRequest("Jane", null)));
    }

    @Test
    void shouldReturnVoidWhenDeleteMember(){
        // given
        var memberId = 1L;

        var member = new Member(1L, "Name", "Surname", Instant.now());
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowedBookRepository.findByMemberIdAndReturnedDateIsNull(1L)).thenReturn(List.of());

        // when
        memberService.deleteMember(memberId);

        // then
        verify(memberRepository, times(1)).delete(member);
    }

    @Test
    void shouldThrowMemberNotFoundByIdExceptionWhenDeletingNonExistingMember() {
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundByIdException.class, () -> memberService.deleteMember(1L));
    }

    @Test
    void shouldReturnPageOfMemberResponsesWhenGettingAllMembers(){
        // given
        var pageable = PageRequest.of(0, 1);
        var memberToFind = new Member(1L, "Name", "Surname", Instant.now());
        when(memberRepository.findAll(eq(pageable))).thenReturn(new PageImpl<>(List.of(memberToFind)));

        // when
        var membersPage = memberService.getAllMembers(pageable);

        // then
        assertNotNull(membersPage);
        assertEquals(1, membersPage.getTotalElements());
        assertEquals(1, membersPage.getTotalPages());
        assertEquals(1, membersPage.getContent().size());

        var memberResponse = membersPage.getContent().getFirst();
        assertEquals(memberToFind.getName(), memberResponse.getName());
        assertEquals(memberToFind.getSurname(), memberResponse.getSurname());
        assertTrue(memberResponse.hasLinks());
        assertTrue(memberResponse.getLinks().hasLink("self"));
        assertTrue(memberResponse.getLinks().hasLink("update"));
        assertTrue(memberResponse.getLinks().hasLink("delete"));
    }

    private Answer1<Member, Member> getFakeSaveAnswer(Long id) {
        return member -> {
            member.setId(id);
            return member;
        };
    }
}
