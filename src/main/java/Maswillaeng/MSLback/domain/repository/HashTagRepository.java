package Maswillaeng.MSLback.domain.repository;

import Maswillaeng.MSLback.domain.entity.HashTag;
import Maswillaeng.MSLback.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    boolean existsByTag(Tag tag);
}
