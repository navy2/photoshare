package cmpg.photoshare.service;

import cmpg.photoshare.entity.Image;
import cmpg.photoshare.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image getImageByImageParentAndPath(String path, String imageParent){
        return imageRepository.getImageByImageParentAndPath(path, imageParent);
    }

    public List<Image> getByImageParent(String path) {
        return imageRepository.getByImageParent(path);
    }

    public void uploadFile(Image image) {
        imageRepository.save(image);
    }

    public void deleteFile(String path){
        imageRepository.deleteByPath(path);
    }

}
