package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Face;
import org.springframework.web.multipart.MultipartFile;

public interface FaceService {
    String save(Face face, MultipartFile file, String facesetToken);

    String update(Face face, MultipartFile file, String facesetToken);

    void delete(String faceToken, String facesetToken);

}
