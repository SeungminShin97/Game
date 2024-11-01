package Seungmin.Game.domain.member;

import Seungmin.Game.common.exceptions.CustomException;
import Seungmin.Game.common.exceptions.CustomExceptionCode;
import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.member.memberDto.MemberRequest;
import Seungmin.Game.domain.member.memberDto.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return memberRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }

    // OAuth 회원 자동 로그인
    /**
     * OAuth 회원 자동 로그인용
     */
    public void autoLogin(MemberResponse memberResponse) {
        String loginId = memberResponse.getLoginId();
        UserDetails userDetails = loadUserByUsername(loginId);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


    // 회원가입
    @Transactional
    public Long saveMember(final MemberRequest memberRequest) {
        try {
            memberRequest.setNickname(memberRequest.getLoginId());
            String encodedPassword = passwordEncoder.encode(memberRequest.getPassword());
            Member member = memberRequest.toEntity(encodedPassword);
            memberRepository.save(member);
            return member.getId();
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCode.MemberSaveFaileException);
        }
    }

    @Transactional
    public Long updateOAuthMemberByIdentifier(final MemberRequest memberRequest) {
        if(memberRequest.getIdentifier() == null)
            throw new IllegalArgumentException("Identifier가 비어 있습니다.");

        Member member = memberRepository.findByIdentifier(memberRequest.getIdentifier()).orElseThrow(() -> new UsernameNotFoundException("유저 검색 실패"));
        member.updateOAuthMember(memberRequest);
        return member.getId();
    }


    /**
     * 아이디 중복 검사
     * 있으면 false, 없으면 true
     * @param loginId
     * @return boolean
     */
    // 아이디 중복 검사
    public boolean existLoginId(final String loginId) {
        return memberRepository.findByLoginId(loginId).isEmpty();
    }

    public MemberResponse getMemberById(final Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("유저 검색 실패"));
        return member.toDto();
    }

    public MemberResponse getMemberByIdentifier(String identifier) {
            Member member = memberRepository.findByIdentifier(identifier).orElse(null);
            if(member != null)
                return member.toDto();
            else
                throw new UsernameNotFoundException("유저 검색 실패");
    }

    public Member getMemberByAuthentication(Authentication authentication) {
        if (confirmAuthenticationIsAnonymous(authentication)) {
            if(authentication instanceof UsernamePasswordAuthenticationToken) {             // 일반 로그인
                String loginId = authentication.getName();
                return memberRepository.findByLoginId(loginId)
                        .orElseThrow(() -> new UsernameNotFoundException("유저 검색 실패"));
            }
            else {                                                                          // oauth2 로그인
                String identifier = authentication.getName();
                return memberRepository.findByIdentifier(identifier).orElseThrow(() -> new UsernameNotFoundException("유저 검색 실패"));
            }
        }
        throw new UsernameNotFoundException("비로그인 유저");
    }

    public boolean confirmAuthenticationIsAnonymous(Authentication authentication) {
        if(!authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken)
            throw new UsernameNotFoundException("비로그인 유저");
        else
            return true;
    }
}
