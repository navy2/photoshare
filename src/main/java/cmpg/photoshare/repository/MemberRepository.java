package cmpg.photoshare.repository;

import cmpg.photoshare.entity.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByEmail(String email);
    List<Member> findByEmailAndHashedPass(String email, String hashedPass);

    @Transactional
    @Modifying
    @Query(value = "UPDATE " +
            "Member m " +
            "SET " +
            "m.name = :name, "+
            "m.surname = :surname, "+
            "m.email = :email "+
            "WHERE "+
            "m.email = :email")
    void updateMember(String name, String surname, String email);

}
