package cmpg.photoshare.service;

import cmpg.photoshare.entity.Image;
import cmpg.photoshare.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ImageService {

    private final StorageService service;
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository, StorageService service) {
        this.imageRepository = imageRepository;
        this.service = service;
    }

    public Image getImageByImageParentAndPath(String path, String imageParent){
        return imageRepository.getImageByImageParentAndPath(path, imageParent);
    }

    public Image getImageByPath(String path){
        return imageRepository.getImageByPath(path);
    }

    public List<Image> getByImageParent(String path) {
        return imageRepository.getByImageParent(path);
    }

    public void uploadImage(Image image) {
        imageRepository.save(image);
    }

    public void deleteImage(String path){
        imageRepository.deleteByPath(path);
    }
}
