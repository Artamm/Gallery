package lt.gallery.services;

import lt.gallery.dao.Picture;
import lt.gallery.dao.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;


public interface PictureService {
    void setValues(Picture picture, String originalFilename);


    byte[] getImageById(Long id);

    byte[] getFullImageById(Long id);

    List<Picture> filterByOneTagLeast(String tags);

    List<Picture> searchbyAll(String value);

    List<Picture> inSensitiveSearch(String value);

    void fillTagList(String[] stringTags, List<Tag> new_tags);

    @PreAuthorize("hasAuthority('ADMIN')")
    void deleteTagAndPicture(Long id);

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    void updateValues(Picture picture, Picture modified);

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    void updateValuesWithImage(Picture picture, Picture modified);

    void postDate(Picture picture);

    List<Picture> getAllPictures();

    Optional<Picture> findPictureById(Long id);

    Picture getOneById(Long id);

    Picture findFirstByFilename(String filename);

    void updatePicture(Picture picture);

    boolean savePicture(Picture picture);
}
