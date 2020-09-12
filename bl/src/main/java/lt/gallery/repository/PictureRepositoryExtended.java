package lt.gallery.repository;

import lt.gallery.dao.Picture;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PictureRepositoryExtended  {
    List<Picture> findByValues(String value);
    //To write queries?

    static Specification<Picture> hasAuthor(String author) {
        return (picture, cq, cb) -> cb.equal(picture.get("author"), author);
    }

    static Specification<Picture> hasFilename(String filename) {
        return (picture, cq, cb) -> cb.like(picture.get("filename"), filename );
    }
    static Specification<Picture> hasText(String text) {
        return (picture, cq, cb) -> cb.like(picture.get("text"), text );
    }

    static Specification<Picture> hasDate(String date) {
        return (picture, cq, cb) -> cb.like(picture.get("date"), date );
    }


}
