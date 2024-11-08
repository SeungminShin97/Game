package Seungmin.Game.domain.member;

import Seungmin.Game.common.enums.Provider;
import Seungmin.Game.domain.member.memberDto.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByIdentifierAndProvider(String identifier, Provider provider);
    Optional<Member> findByIdentifier(String identifier);
}
