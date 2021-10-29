package cmpg.photoshare.service;

import cmpg.photoshare.entity.MemberImage;
import cmpg.photoshare.repository.MemberImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberImageService {

    @Autowired
    private MemberImageRepository memberImageRepository;

    public void addMemberImage(MemberImage memberImage){
        memberImageRepository.save(memberImage);
    }

    public List<MemberImage> getByEmail(String email){
        return memberImageRepository.getByEmail(email);
    }

    public void deleteByPathAndEmail(String path, String email){
        memberImageRepository.deleteByPathAndEmail(path, email);
    }

    public void deleteByPath(String path){
        memberImageRepository.deleteByPath(path);
    }

}