package lt.gallery.repository.impl;

import lt.gallery.dao.Picture;
import lt.gallery.repository.PictureRepositoryExtended;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PictureRepositoryExtendedImpl implements PictureRepositoryExtended {
    @PersistenceContext
    private EntityManager em;


    public List<Picture> findByValues(String value) {
        value=value.toLowerCase();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Picture> cq_Picture = cb.createQuery(Picture.class);

        Root<Picture> pictureDB = cq_Picture.from(Picture.class);

        Expression<String> author = pictureDB.get("author");
        Expression<String> filename = pictureDB.get("filename");
        Expression<String> text = pictureDB.get("text");

        cq_Picture.where(cb.or(
                cb.equal(cb.lower(author),value),
                cb.equal(cb.lower(filename),value),
                cb.equal(cb.lower(text),value)
        ));


        TypedQuery<Picture> query = em.createQuery(cq_Picture);
        return query.getResultList();

    }


}
