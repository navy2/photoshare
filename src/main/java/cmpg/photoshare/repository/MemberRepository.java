package cmpg.photoshare.repository;

import cmpg.photoshare.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByEmail(String email);
    List<Member> findByEmailAndHashedPass(String email, String hashedPass);
}
