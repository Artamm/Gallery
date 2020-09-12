package lt.gallery.services.impl;

import com.google.common.collect.Iterables;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lt.gallery.dao.Picture;
import lombok.Data;
import lt.gallery.dao.Tag;
import lt.gallery.repository.TagRepository;
import lt.gallery.services.PictureService;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import lt.gallery.repository.PictureRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static lt.gallery.constUtil.ConstantFormat.*;
import static lt.gallery.repository.PictureRepositoryExtended.hasAuthor;
import static lt.gallery.repository.PictureRepositoryExtended.hasFilename;
import static lt.gallery.repository.PictureRepositoryExtended.hasDate;
import static lt.gallery.repository.PictureRepositoryExtended.hasText;
import static org.springframework.data.jpa.domain.Specification.where;


@Data
@CommonsLog
@RequiredArgsConstructor
@Service("pictureService")
@Transactional
public class PictureServiceImpl implements PictureService {

    @Autowired
    private final PictureRepository pictureRepository;
    @Autowired
    private final TagRepository tagRepository;


    //    Create methods
    public void postDate(Picture picture) {
        picture.setDate(LocalDateTime
                .now()
                .format(
                        DateTimeFormatter.ofPattern(DATE_FORMAT)
                ));
    }


    public void setValues(Picture picture, String originalFilename) {
//        Add current date
        postDate(picture);

//        Set filename from the form.
//        If form field was empty - sets by default from original file
        if (picture.getFilename().isEmpty()) {
            picture.setFilename(originalFilename);
        } else {
            picture.setFilename(
                    picture.getFilename()
            );
        }
//         Create thumbnail and add tags from the form
        createThumbnail(picture.getFullImage(), picture);
        addTags(picture.getStringTags(), picture);
        pictureRepository.save(picture);

    }


    private void addTags(String tags, Picture picture) {
        if (!tags.isEmpty()) {
            List<Tag> pictureTags = new ArrayList<>();
            String[] stringTags = tags.split(",");
            fillTagList(stringTags, pictureTags);
            picture.setTags(pictureTags);
        }
    }

    private void createThumbnail(byte[] bytes, Picture picture) {
//        try {
//
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            BufferedImage img = ImageIO.read(in);
            BufferedImage thumbnailImage = Scalr.resize(img, IMG_WIDTH, IMG_HEIGHT);

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                //Need to choose formats by itself
                ImageIO.write(thumbnailImage, "png", baos);
                baos.flush();
                picture.setThumbnail(baos.toByteArray());
            }
        } catch (IOException e) {
            log.error("From bytes to bufferImage in  thumbnail()  -> ");
        }

    }


//  Search

    /* Not-case-sensitive tag(s) search*/
    public List<Picture> filterByOneTagLeast(String tags) {
        if (!tags.isEmpty()) {
            List<Tag> pictureTags = new ArrayList<>();
            String[] stringTags = tags.split(",");
            List<Picture> pictures = new ArrayList<>();

            for (String stringTag : stringTags) {
                if (!(tagRepository.findTagsByName(stringTag.toLowerCase()).isEmpty())) {//if not empty
                    Tag tag = tagRepository.findTagByName(stringTag.toLowerCase());
                    pictureTags.add(tag);
                }

                for (Tag tag : pictureTags) {
                    List<Tag> dummy = new ArrayList<>();
                    dummy.add(tag);

                    //list.contains(Iterable<T>collection)?
                    if (!(pictures.contains(pictureRepository.findByTags(dummy)))) {
                        pictureRepository.findByTags(dummy).forEach(pictures::add);
                    }
                    dummy.remove(tag);
                }

            }

            return pictures;
        } else {
            return pictureRepository.findAll();
        }

    }

    public List<Picture> searchbyAll(String value) {
        if (value.isEmpty()) {
            return pictureRepository.findAll();
        } else {

            return pictureRepository.findAll(where(hasAuthor(value))
                    .or(where(hasFilename(value)))
                    .or(where(hasText(value)))
                    .or(where(hasDate(value)))
            );

        }
    }

    public List <Picture> inSensitiveSearch(String value){
        if (value.isEmpty()) {
            return pictureRepository.findAll();
        } else {

            return pictureRepository.findByValues(value);

        }
    }


//  Display thumbnail and full image
    public byte[] getImageById(Long id) {
        Optional<Picture> ob = pictureRepository.findById(id);
        if (ob.isPresent()) {
            return ob.get().getThumbnail();
        } else {
            // make default picture
            return pictureRepository.findFirstByFilename("nothing.png").getThumbnail();
        }

    }

    public byte[] getFullImageById(Long id) {
        Optional<Picture> ob = pictureRepository.findById(id);
        if (ob.isPresent()) {
            return ob.get().getFullImage();
        } else {
            // make default picture
            return pictureRepository.findFirstByFilename("unknown.png").getFullImage();
        }
    }


//    Delete and update
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteTagAndPicture(Long id) {

        Optional<Picture> picture = pictureRepository.findById(id);
        if (picture.isPresent()) {
            List<Tag> picture_tags = picture.get().getTags();
            pictureRepository.deleteById(id);

            for (Tag tag : picture_tags) {
                List<Tag> dummy = new ArrayList<>();
                dummy.add(tag);

                if ((Iterables.size(pictureRepository.findByTags(dummy)) == 0)) {
                    tagRepository.deleteById(tag.getId());
                }
                dummy.remove(tag);
            }
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public void updateValues(Picture picture, Picture modified) {
        picture.setFilename(modified.getFilename());
        picture.setAuthor(modified.getAuthor());
        picture.setText(modified.getText());
        postDate(picture);
        updateTags(picture, modified);
        picture.setDate(picture.getDate() + " (edited)");
        pictureRepository.save(picture);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public void updateValuesWithImage(Picture picture, Picture modified) {
        updateValues(picture, modified);
        createThumbnail(picture.getFullImage(), picture);
        pictureRepository.save(picture);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    private void updateTags(Picture picture, Picture modified) {
        if (!modified.getStringTags().isEmpty() | modified.getStringTags()!=null) {
            List<Picture> pictures = new ArrayList<>();
            pictures.add(picture);
            List<Tag> current_tags = tagRepository.findTagsByPictures(pictures);
            List<Tag> new_tags = new ArrayList<>();

            String[] stringTags = modified.getStringTags().split(",");

            fillTagList(stringTags, new_tags);

            //delete useless tag from picture tag list
            for (Tag current_tag : current_tags) {
                if (!(Arrays.asList(stringTags).contains(current_tag.getName()))) {
                    picture.getTags().remove(current_tag);
                }
            }
            //add new tags
            for (Tag new_tag : new_tags) {
                if (!(current_tags.contains(new_tag))) {
                    current_tags.add(new_tag);
                }
            }
            picture.setTags(current_tags);
            picture.setStringTags(modified.getStringTags());
            pictureRepository.save(picture);
        }
    }


//    Get list of tags from string output
    public void fillTagList(String[] stringTags, List<Tag> new_tags) {
        for (String stringTag : stringTags) {
            /*Check if tag exist*/
            if (tagRepository.findTagsByName(stringTag.toLowerCase()).isEmpty()) { //Create new Tag if list is empty
                Tag tag = new Tag(stringTag.toLowerCase());
                new_tags.add(tag);
            } else {//Search in repository if not
                Tag tag = tagRepository.findTagByName(stringTag.toLowerCase());
                new_tags.add(tag);
            }
        }
    }



//    Implementing repository methods
    public List<Picture> getAllPictures() {
        return pictureRepository.findAll();
    }

    public Optional<Picture> findPictureById(Long id) {
        return pictureRepository.findById(id);
    }

    public Picture getOneById(Long id) {
        return pictureRepository.getOne(id);
    }

    public Picture findFirstByFilename(String filename) {
        return pictureRepository.findFirstByFilename(filename);
    }

    @Override
    public void updatePicture(Picture picture) {
        postDate(picture);
        picture.setDate(picture.getDate() + " (edited)");
        createThumbnail(picture.getFullImage(),picture);
        updateTags(pictureRepository.getOne(picture.getId()),picture);
        pictureRepository.save(picture);
    }

    public boolean savePicture(Picture picture){
        pictureRepository.save(picture);
        return true;
    }


}
