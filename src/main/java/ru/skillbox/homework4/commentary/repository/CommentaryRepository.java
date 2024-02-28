package ru.skillbox.homework4.commentary.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.homework4.commentary.model.Commentary;
import java.util.List;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long> {

    List<Commentary> findAllByNewsId(Long newsId, Pageable pageable);

    Boolean existsByIdAndNewsIdAndUserId(Long id, Long newsId, Long userId);

    @Query(value = """
    SELECT case when count(c)>0 then true else false end
    FROM commentaries AS c
    WHERE  c.id = :commentaryId and c.user_id = :userId
           """, nativeQuery = true)
    Boolean checkRights(@Param("commentaryId") Long commentaryId, @Param("userId") Long userId);

//    @Query(value = """
//           SELECT case when count(c)>0 then true else false end
//            FROM commentaries AS c JOIN news AS n on :newsId = :newsId
//            JOIN users u on :userId = :userId
//            WHERE c.user_id = :userId
//           """, nativeQuery = true)
//    Boolean viewRights(@Param("commentaryId") Long commentaryId,
//                       @Param("newsId") Long newsId,
//                       @Param("userId") Long userId);
    //SELECT case when count(c)>0 then true else false end
    //FROM commentaries AS c
    //WHERE c.id = 2 and c.user_id = 1
}
