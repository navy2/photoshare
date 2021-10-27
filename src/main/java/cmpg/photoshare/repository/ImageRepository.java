package cmpg.photoshare.repository;

import cmpg.photoshare.entity.Image;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ImageRepository extends CrudRepository<Image, Long>{

    Image getImageByImageParentAndPath(String path, String imageParent);
    List<Image> getByImageParent(String path);

    @Transactional
    @Modifying
    public void deleteByPath(String path);
}
