package com.stepaniuk.nerdysoft.member;

import com.stepaniuk.nerdysoft.member.payload.MemberResponse;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MemberMapper {
    @BeanMapping(qualifiedByName = "addLinks")
    MemberResponse toResponse(Member member);

    @AfterMapping
    @Named("addLinks")
    default MemberResponse addLinks(Member member, @MappingTarget MemberResponse response) {
        response.add(Link.of("/members/" + member.getId()).withSelfRel());
        response.add(Link.of("/members/" + member.getId()).withRel("update"));
        response.add(Link.of("/members/" + member.getId()).withRel("delete"));

        return response;
    }
}
