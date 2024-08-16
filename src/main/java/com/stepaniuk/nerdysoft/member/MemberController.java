package com.stepaniuk.nerdysoft.member;

import com.stepaniuk.nerdysoft.member.payload.MemberCreateRequest;
import com.stepaniuk.nerdysoft.member.payload.MemberResponse;
import com.stepaniuk.nerdysoft.member.payload.MemberUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping(path = "/members", produces = "application/json")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody @Valid MemberCreateRequest memberRequest) {
        return  new ResponseEntity<>(memberService.createMember(memberRequest),
                HttpStatus.CREATED);
    }

    @GetMapping
    public  ResponseEntity<Page<MemberResponse>> getAllMembers(Pageable pageable) {
        return ResponseEntity.ok(memberService.getAllMembers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id){
        return ResponseEntity.ok(memberService.getMember(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody @Valid MemberUpdateRequest memberRequest){
        return ResponseEntity.ok(memberService.updateMember(id, memberRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id){
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
