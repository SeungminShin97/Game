package Seungmin.Game.domain.member;

import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.member.memberDto.MemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return memberRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }

    // 회원가입
    @Transactional
    public boolean saveMember(final MemberRequest memberRequest) {
        try {
            memberRequest.setNickname(memberRequest.getLoginId());
            String encodedPassword = passwordEncoder.encode(memberRequest.getPassword());
            Member member = memberRequest.toEntity(encodedPassword);
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 회원가입 아이디 중복 검사
     * 있으면 false, 없으면 true
     * @param loginId
     * @return
     */
    // 회원가입 아이디 중복 검사
    public boolean findLoginId(final String loginId) {
        return memberRepository.findByLoginId(loginId).isEmpty();
    }



}
