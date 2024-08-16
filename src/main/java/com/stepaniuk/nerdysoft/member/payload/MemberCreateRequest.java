package com.stepaniuk.nerdysoft.member.payload;

import com.stepaniuk.nerdysoft.validation.member.Name;
import com.stepaniuk.nerdysoft.validation.member.Surname;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MemberCreateRequest {
    @NotNull
    @Name
    String name;

    @NotNull
    @Surname
    String surname;
}
