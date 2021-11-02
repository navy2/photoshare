package cmpg.photoshare.controller;

import cmpg.photoshare.service.ImageService;
import cmpg.photoshare.service.MemberImageService;
import cmpg.photoshare.service.MemberService;
import cmpg.photoshare.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping(path = "/images")
public class ImageController {

    private final ImageService imageService;
    private final MemberService memberService;
    private final MemberImageService memberImageService;
    private final StorageService service;

    @Autowired
    public ImageController(ImageService imageService, MemberService memberService, MemberImageService memberImageService, StorageService service) {
        this.imageService = imageService;
        this.memberService = memberService;
        this.memberImageService = memberImageService;
        this.service = service;
    }

}