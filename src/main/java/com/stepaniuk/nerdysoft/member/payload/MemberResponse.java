package com.stepaniuk.nerdysoft.member.payload;

import com.stepaniuk.nerdysoft.validation.member.Name;
import com.stepaniuk.nerdysoft.validation.member.Surname;
import com.stepaniuk.nerdysoft.validation.shared.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class MemberResponse extends RepresentationModel<MemberResponse> {
    @NotNull
    @Id
    private final Long id;
    @NotNull
    @Name
    private final String name;
    @NotNull
    @Surname
    private final String surname;
    @NotNull
    private final Instant membershipDate;
}
