package cmpg.photoshare.repository;

import cmpg.photoshare.entity.MemberImage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface MemberImageRepository extends CrudRepository<MemberImage, Long> {

    List<MemberImage> getByEmail(String email);

    @Transactional
    @Modifying
    public void deleteByPath(String path);

    @Transactional
    @Modifying
    public void deleteByPathAndEmail(String path, String email);
}
