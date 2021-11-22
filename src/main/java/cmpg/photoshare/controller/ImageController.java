package cmpg.photoshare.controller;

import cmpg.photoshare.service.ImageService;
import cmpg.photoshare.service.MemberImageService;
import cmpg.photoshare.service.MemberService;
import cmpg.photoshare.service.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import cmpg.photoshare.entity.Image;
import cmpg.photoshare.entity.Member;
import cmpg.photoshare.entity.MemberImage;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.http.entity.ContentType.*;

@Controller
@CrossOrigin(origins = "https://photoshare-react.herokuapp.com", allowedHeaders = "*", allowCredentials = "true")
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
    private static final String awsurl = "https://photoshare-cmpg.s3.us-east-2.amazonaws.com/";

    @PostMapping(path="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public ResponseEntity<Image> imageUpload(@RequestParam("image") MultipartFile multipartFile,
                                             @RequestParam("title") String title,
                                             @RequestParam("imageParent")String imageParent,
                                             @RequestParam("geolocation")String geolocation,
                                             @RequestParam("tags") String tags,
                                             @RequestParam("capturedBy") String capturedBy,
                                             @RequestParam("capturedDate") String capturedDate,
                                             HttpSession session) throws JSONException {
        final String email = (String) session.getAttribute("email");
        Image newImage = new Image();
        String fileName = multipartFile.getOriginalFilename();

        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        System.out.println(extension);

        if(!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("ico")
                && !extension.equals("gif") && !extension.equals("tiff") && !extension.equals("bmp"))
        {return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);}

        try{
            String path = "";
            path = email + "/" + multipartFile.getOriginalFilename();
            String dbpath = awsurl+ path;
            service.uploadFile(path,multipartFile);

            newImage.setTitle(title);
            newImage.setFileName(fileName);
            newImage.setPath(dbpath);
            newImage.setGeoLocation(geolocation);
            newImage.setTags(tags);
            newImage.setCapturedBy(capturedBy);
            newImage.setCapturedDate(capturedDate);
            newImage.setIsImage("T");
            newImage.setImageParent(imageParent);

            imageService.uploadImage(newImage);
            MemberImage memberImage = new MemberImage();
            memberImage.setEmail(email);
            memberImage.setPath(dbpath);
            memberImageService.addMemberImage(memberImage);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<Image>(newImage, HttpStatus.OK);
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Image>> getMemberFiles(HttpSession httpSession){
        String email = (String) httpSession.getAttribute("email");
        if(email==null){
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }
        List<MemberImage> memberImageList = memberImageService.getByEmail(email);
        List<Image> imageList = new ArrayList<>();

        for(MemberImage memberImage: memberImageList){
            Image image = imageService.getImageByPath(memberImage.getPath());
            if(image!=null)
            {
                imageList.add(image);
            }
            else{System.out.println("image list empty");}
        }
        return new ResponseEntity(imageList, HttpStatus.OK);
    }

    @PostMapping(path = "/sharefile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> shareFile(@RequestBody String data, HttpSession session) throws JSONException{

        JSONObject jsonObject = new JSONObject(data);
        Gson gson = new Gson();
        JSONObject imageData = (JSONObject) jsonObject.get("filedata");
        Image image = gson.fromJson(imageData.toString(), Image.class);
        String shareWithEmail = jsonObject.getString("shareEmail");
        Member member = memberService.getMember(shareWithEmail);

        if(member == null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        String email = (String) session.getAttribute("email");

        if(email==null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);

        MemberImage memberImage = new MemberImage();
        memberImage.setEmail(shareWithEmail);
        memberImage.setPath(image.getPath());
        memberImageService.addMemberImage(memberImage);

        return new ResponseEntity(null, HttpStatus.OK);
    }

    @PostMapping(path= "/createalbum", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Image> createAlbum(@RequestBody String data, HttpSession session) throws JSONException {

        JSONObject jsonObject = new JSONObject(data);
        String albumName =  jsonObject.getString("filename");
        String albumParent = jsonObject.getString("fileparent");
        String email = (String) session.getAttribute("email");

        if(email == null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);

        String albumdbPath = awsurl + email + "/" + albumName;
        Image image = new Image();

        image.setFileName(albumName);
        image.setPath(albumdbPath);
        image.setCapturedBy(email);
        image.setImageParent(albumParent);
        image.setIsImage("F");
        String albumPath = email +"/"+ albumName;
        service.createFolder(albumPath);
        imageService.uploadImage(image);

        MemberImage memberImage = new MemberImage();
        memberImage.setEmail(email);
        memberImage.setPath(albumdbPath);
        memberImageService.addMemberImage(memberImage);

        return new ResponseEntity(image, HttpStatus.OK);
    }

    @GetMapping(path = "/getalbumimages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Image>> getImagesInFolder(@RequestParam String path){
        List<Image> imageList = imageService.getByImageParent(path);

        return new ResponseEntity(imageList, HttpStatus.OK);
    }

    @PostMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFile(@RequestBody Image image, HttpSession session) throws JSONException {
        String email = (String) session.getAttribute("email");
        if (email == null)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        String path = email + "/" + image.getFileName();

        try {
            service.deleteFile(path);
            memberImageService.deleteByPath(image.getPath());
            imageService.deleteImage(image.getPath());

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(null, HttpStatus.OK);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName, HttpSession session) {
        String email = (String) session.getAttribute("email");
        String path = email + "/" + fileName;
        byte[] data = service.downloadFile(path);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}