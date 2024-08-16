package com.stepaniuk.nerdysoft.member.payload;

import com.stepaniuk.nerdysoft.validation.member.Name;
import com.stepaniuk.nerdysoft.validation.member.Surname;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MemberUpdateRequest {
    @Nullable
    @Name
    String name;
    @Nullable
    @Surname
    String surname;
}
