package Maswillaeng.MSLback.domain.repository;

import Maswillaeng.MSLback.domain.entity.Follow;
import Maswillaeng.MSLback.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * 팔로우, 팔로워 개수 확인
     * @param user
     * @return
     */
    Long countByFollowing(User user);
    Long countByFollower(User user);

    /**
     * 내가 이 사람을 팔로우하고 있는지 확인
     */
    Optional<Follow> findByFollowerIdAndFollowingId(Long myId, Long userId);

    /**
     * 팔로우, 팔로워 목록 불러오기
     * 존재하지 않을 경우, null Exception을 방지하기 위해 List를 사용.
     */
//    List<Follow> getFollowList(Long userId);
//    List<Follow> getFollowerList(Long userId);
}
