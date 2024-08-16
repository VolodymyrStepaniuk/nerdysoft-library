package com.stepaniuk.nerdysoft.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stepaniuk.nerdysoft.member.exception.MemberNotFoundByIdException;
import com.stepaniuk.nerdysoft.member.payload.MemberCreateRequest;
import com.stepaniuk.nerdysoft.member.payload.MemberResponse;
import com.stepaniuk.nerdysoft.member.payload.MemberUpdateRequest;
import com.stepaniuk.nerdysoft.testspecific.ControllerLevelUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static com.stepaniuk.nerdysoft.testspecific.hamcrest.TemporalStringMatchers.instantComparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerLevelUnitTest(controllers = MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    void shouldReturnMemberResponseWhenCreatingMember() throws Exception {
        // given
        var memberCreateRequest = new MemberCreateRequest("Name", "Surname");
        var memberResponse = new MemberResponse(1L, memberCreateRequest.getName(), memberCreateRequest.getSurname(), Instant.now());
        memberResponse.add(Link.of("http://localhost/members/1", "self"));
        memberResponse.add(Link.of("http://localhost/members/1", "update"));
        memberResponse.add(Link.of("http://localhost/members/1", "delete"));

        when(memberService.createMember(memberCreateRequest)).thenReturn(memberResponse);

        // when & then
        mockMvc.perform(post("/members")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(memberCreateRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(memberResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(memberResponse.getName())))
                .andExpect(jsonPath("$.surname", is(memberResponse.getSurname())))
                .andExpect(jsonPath("$.membershipDate", instantComparesEqualTo(memberResponse.getMembershipDate())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/members/1")))
                .andExpect(jsonPath("$._links.update.href", is("http://localhost/members/1")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/members/1")));;
    }

    @Test
    void shouldReturnErrorWhenCreatingMemberWithWrongName() throws Exception {
        // given
        var memberCreateRequest = new MemberCreateRequest("", "Surname");

        // when & then
        mockMvc.perform(post("/members")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(memberCreateRequest))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnPageOfMemberResponsesWhenGettingAllMembers() throws Exception {
        // given
        var response = new MemberResponse(1L, "Name", "Surname", Instant.now());
        response.add(Link.of("http://localhost/members/1", "self"));
        response.add(Link.of("http://localhost/members/1", "update"));
        response.add(Link.of("http://localhost/members/1", "delete"));

        var page = new PageImpl<>(List.of(response), PageRequest.of(0, 1), 1);

        when(memberService.getAllMembers(PageRequest.of(0, 1))).thenReturn(page);

        // when & then
        mockMvc.perform(get("/members")
                .contentType("application/json")
                .param("page", "0")
                .param("size", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.content[0].name", is(response.getName())))
                .andExpect(jsonPath("$.content[0].surname", is(response.getSurname())))
                .andExpect(jsonPath("$.content[0].membershipDate", instantComparesEqualTo(response.getMembershipDate())))
                .andExpect(jsonPath("$.content[0].links[0].href", is("http://localhost/members/1")))
                .andExpect(jsonPath("$.content[0].links[1].href", is("http://localhost/members/1")))
                .andExpect(jsonPath("$.content[0].links[2].href", is("http://localhost/members/1")));
    }

    @Test
    void shouldReturnMemberWhenGettingMemberById() throws Exception {
        // given
        var response = new MemberResponse(1L, "Name", "Surname", Instant.now());
        response.add(Link.of("http://localhost/members/1", "self"));
        response.add(Link.of("http://localhost/members/1", "update"));
        response.add(Link.of("http://localhost/members/1", "delete"));

        when(memberService.getMember(1L)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/members/1")
                .contentType("application/json")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(response.getName())))
                .andExpect(jsonPath("$.surname", is(response.getSurname())))
                .andExpect(jsonPath("$.membershipDate", instantComparesEqualTo(response.getMembershipDate())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/members/1")))
                .andExpect(jsonPath("$._links.update.href", is("http://localhost/members/1")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/members/1")));
    }

    @Test
    void shouldReturnErrorResponseWhenGetMemberByIdAndMemberNotFound() throws Exception {
        // given
        doThrow(new MemberNotFoundByIdException(1L)).when(memberService).getMember(1L);

        // when & then
        mockMvc.perform(get("/members/1")
                .contentType("application/json")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Member not found")))
                .andExpect(jsonPath("$.detail", is("Member with id 1 not found")))
                .andExpect(jsonPath("$.instance", is("/members/1")));
    }

    @Test
    void shouldReturnMemberWhenUpdatingMember() throws Exception {
        // given
        var memberUpdateRequest = new MemberUpdateRequest("New Name", "New Surname");
        var response = new MemberResponse(1L, memberUpdateRequest.getName(), memberUpdateRequest.getSurname(), Instant.now());
        response.add(Link.of("http://localhost/members/1", "self"));
        response.add(Link.of("http://localhost/members/1", "update"));
        response.add(Link.of("http://localhost/members/1", "delete"));

        when(memberService.updateMember(1L, memberUpdateRequest)).thenReturn(response);

        // when & then
        mockMvc.perform(patch("/members/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(memberUpdateRequest))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(response.getName())))
                .andExpect(jsonPath("$.surname", is(response.getSurname())))
                .andExpect(jsonPath("$.membershipDate", instantComparesEqualTo(response.getMembershipDate())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/members/1")))
                .andExpect(jsonPath("$._links.update.href", is("http://localhost/members/1")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/members/1")));
    }

    @Test
    void shouldReturnErrorWhenUpdatingMemberWithWrongName() throws Exception {
        // given
        var memberUpdateRequest = new MemberUpdateRequest("", "Surname");

        // when & then
        mockMvc.perform(patch("/members/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(memberUpdateRequest))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnErrorResponseWhenUpdateMemberByIdAndMemberNotFound() throws Exception {
        // given
        var memberUpdateRequest = new MemberUpdateRequest("New Name", "New Surname");

        doThrow(new MemberNotFoundByIdException(1L)).when(memberService).updateMember(1L, memberUpdateRequest);

        // when & then
        mockMvc.perform(patch("/members/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(memberUpdateRequest))
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Member not found")))
                .andExpect(jsonPath("$.detail", is("Member with id 1 not found")))
                .andExpect(jsonPath("$.instance", is("/members/1")));;
    }

    @Test
    void shouldReturnNoContentWhenDeletingMember() throws Exception {
        // given
        // when & then
        mockMvc.perform(delete("/members/1")
                .contentType("application/json")
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnErrorResponseWhenDeleteMemberByIdAndMemberNotFound() throws Exception {
        // given
        doThrow(new MemberNotFoundByIdException(1L)).when(memberService).deleteMember(1L);

        // when & then
        mockMvc.perform(delete("/members/1")
                .contentType("application/json")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Member not found")))
                .andExpect(jsonPath("$.detail", is("Member with id 1 not found")))
                .andExpect(jsonPath("$.instance", is("/members/1")));
    }
}
