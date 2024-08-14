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


    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if(member == null)
            return null;
        else
            return member.toDto();
    }


    // 카카오 정보로 회원 검색
    public MemberResponse getMemberByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElse(null);
        if(member == null)
            return null;
        else
            return member.toDto();
    }


    public MemberResponse getMemberByEmail(final String email) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member != null)
            return member.toDto();
        else
            return null;
    }

    public Member getMemberByAuthentication(Authentication authentication) {
        if (confirmAuthenticationIsAnonymous(authentication)) {
            String loginId = authentication.getName();
            return memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new UsernameNotFoundException("유저 검색 실패"));
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
