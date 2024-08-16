package com.stepaniuk.nerdysoft.member;

import com.stepaniuk.nerdysoft.book.borrowed.BorrowedBookRepository;
import com.stepaniuk.nerdysoft.member.exception.MemberHasBorrowedBooksException;
import com.stepaniuk.nerdysoft.member.exception.MemberNotFoundByIdException;
import com.stepaniuk.nerdysoft.member.payload.MemberCreateRequest;
import com.stepaniuk.nerdysoft.member.payload.MemberResponse;
import com.stepaniuk.nerdysoft.member.payload.MemberUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BorrowedBookRepository borrowedBookRepository;
    private final MemberMapper memberMapper;

    public MemberResponse createMember(MemberCreateRequest memberRequest) {
        Member newMember = new Member();
        newMember.setName(memberRequest.getName());
        newMember.setSurname(memberRequest.getSurname());

        var savedMember = memberRepository.save(newMember);
        return memberMapper.toResponse(savedMember);
    }

    public MemberResponse getMember(Long id) {
        var member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundByIdException(id));
        return memberMapper.toResponse(member);
    }

    public Page<MemberResponse> getAllMembers(Pageable pageable){
        return memberRepository.findAll(pageable)
                .map(memberMapper::toResponse);
    }

    public MemberResponse updateMember(Long id, MemberUpdateRequest memberRequest) {
        var memberToUpdate = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundByIdException(id));

        if (memberRequest.getName() != null) {
            memberToUpdate.setName(memberRequest.getName());
        }
        if (memberRequest.getSurname() != null) {
            memberToUpdate.setSurname(memberRequest.getSurname());
        }

        var updatedMember = memberRepository.save(memberToUpdate);

        return memberMapper.toResponse(updatedMember);
    }

    public void deleteMember(Long id) {
        if (!borrowedBookRepository.findByMemberIdAndReturnedDateIsNull(id).isEmpty()) {
            throw new MemberHasBorrowedBooksException(id);
        }

        var memberToDelete = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundByIdException(id));

        memberRepository.delete(memberToDelete);
    }
}
