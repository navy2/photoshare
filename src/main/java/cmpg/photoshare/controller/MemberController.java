package cmpg.photoshare.controller;

import cmpg.photoshare.entity.Member;
import cmpg.photoshare.service.MemberService;
import cmpg.photoshare.service.StorageService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Controller
@CrossOrigin(origins = "https://photoshare-react.herokuapp.com", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping(path="/users")
public class MemberController {

    @Value("${application.bucket.name}")
    private String bucketName;

    private final MemberService memberService;
    private final StorageService service;

    @Autowired
    public MemberController(MemberService memberService, StorageService service) {
        this.memberService = memberService;
        this.service = service;
    }

    @GetMapping(path="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Iterable<Member> getAllMembers(){
        return memberService.getAllMembers();
    }

    @PostMapping(path ="/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> AddMember(@RequestBody Member member){

        String email = member.getEmail();
        Member doesExist = memberService.getMember(email);

        if(doesExist!=null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);

        Member newMember = new Member();

        String password = member.getHashedPass();
        String hashToStore = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
        newMember.setName(member.getName());
        newMember.setSurname(member.getSurname());
        newMember.setEmail(member.getEmail());
        newMember.setHashedPass(hashToStore);

        memberService.addMember(newMember);

        if(newMember == null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String path = email;
        service.createFolder(path);

        System.out.println("Folder Created in AWS bucket");
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @PostMapping(path="/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody Member member, HttpSession session)
        throws JSONException{

        session.setAttribute("email", member.getEmail());
        String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(member.getHashedPass());
        List<Member> mem = memberService.login(member.getEmail(), hash);
        if(mem.size()>0)
            return new ResponseEntity(true, HttpStatus.OK);
        return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Member> getMember(HttpSession session) throws JSONException{

        String email = (String) session.getAttribute("email");
        Member mem = memberService.getMember(email);
        if(mem!=null)
        {
            return new ResponseEntity(mem, HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(path="/updatemember", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMember(@RequestBody Member member, HttpSession session) throws JSONException{

        try{
            memberService.updateMember(member);
            return new ResponseEntity(null, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path="/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> logout(HttpSession session){
        String email = (String) session.getAttribute("email");
        session.invalidate();
        return new ResponseEntity(HttpStatus.OK);
    }
}
