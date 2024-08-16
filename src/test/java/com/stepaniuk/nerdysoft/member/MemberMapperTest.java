package com.stepaniuk.nerdysoft.member;

import com.stepaniuk.nerdysoft.member.payload.MemberResponse;
import com.stepaniuk.nerdysoft.testspecific.MapperLevelUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@MapperLevelUnitTest
@ContextConfiguration(classes = {MemberMapperImpl.class})
public class MemberMapperTest {
    @Autowired
    private MemberMapper memberMapper;

    @Test
    void shouldMapMemberToMemberResponse() {
        // given
        Member memberToMap = new Member(1L, "John", "Doe", Instant.now());
        // when
        MemberResponse memberResponse = memberMapper.toResponse(memberToMap);
        // then
        assertNotNull(memberResponse);
        assertEquals(memberToMap.getId(), memberResponse.getId());
        assertEquals(memberToMap.getName(), memberResponse.getName());
        assertEquals(memberToMap.getSurname(), memberResponse.getSurname());
        assertEquals(memberToMap.getMembershipDate(), memberResponse.getMembershipDate());
        assertTrue(memberResponse.hasLinks());
        assertTrue(memberResponse.getLinks().hasLink("self"));
        assertTrue(memberResponse.getLinks().hasLink("update"));
        assertTrue(memberResponse.getLinks().hasLink("delete"));
    }
}
