package Seungmin.Game.domain.member;

import Seungmin.Game.domain.member.memberDto.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);
}
