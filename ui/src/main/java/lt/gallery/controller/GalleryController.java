package lt.gallery.controller;

import lombok.extern.apachecommons.CommonsLog;
import lt.gallery.dao.Picture;

import lt.gallery.dao.User;
import lt.gallery.services.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static lt.gallery.constUtil.ConstantFormat.DEFAULT_FILENAME;


@Controller
@RequestMapping
@CommonsLog
public class GalleryController {


    private final PictureService pictureService;

    public GalleryController(PictureService pictureService) {
        this.pictureService = pictureService;

    }

    @GetMapping
    public String gallery(Model model) {
        model.addAttribute("picture", new Picture());
        model.addAttribute("pictures", pictureService.getAllPictures());
        return "gallery";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, params = "action=Save")
    public String create(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute Picture picture
    ) throws IOException {
        if (file.isEmpty()) {
            picture.setFullImage(pictureService.findFirstByFilename("unknown.png").getFullImage());
            picture.setOwner(user);
            pictureService.setValues(picture, DEFAULT_FILENAME);
        } else {
            String originalFilename = file.getOriginalFilename();
            try {
                picture.setFullImage(file.getBytes());
                picture.setOwner(user);
                pictureService.setValues(picture, originalFilename);
            } catch (IOException e) {
                log.error("Impossible to get bytes from Multipart file. See this -> ", e);
            }
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, params = "action=Update")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public String update(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute Picture modified
    ) {
        if (modified.getId() != null) {
            Picture picture = pictureService.getOneById(modified.getId());
            if (file.isEmpty()) {
                pictureService.updateValues(picture, modified);

            } else {
                try {
                    picture.setFullImage(file.getBytes());
                } catch (Exception e) {
                    log.error("Impossible to get bytes from Multipart file. See this -> ", e);
                }
                pictureService.updateValuesWithImage(picture, modified);

            }
        }

        return "redirect:/";
    }

    @DeleteMapping(value = "/deletePicture/{id}", params = "action=Delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        pictureService.deleteTagAndPicture(id);
        return "redirect:/";
    }

    @PutMapping(value = "/updatePicture/{id}", params = "action=Edit")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public String setPicture(@PathVariable("id") Long id, Picture picture, Model model) {
        Picture pic = pictureService.getOneById(id);
        model.addAttribute("picture", pic);
        Iterable<Picture> pictures = pictureService.getAllPictures();
        model.addAttribute("pictures", pictures);

        return "/gallery";
    }

    @PostMapping(value = "/filter",params="action=Exact_Search")
    public String filterAtLeastOne(@ModelAttribute Picture picture, Model model) {
        if (!picture.getStringTags().isEmpty() & picture.getText().isEmpty()) {
            Iterable<Picture> pictures = pictureService.filterByOneTagLeast(picture.getStringTags());
            model.addAttribute("pictures", pictures);

        } else {
            Iterable<Picture> pictures = pictureService.searchbyAll(picture.getText());

            model.addAttribute("pictures", pictures);
        }

        return "/gallery";
    }

    @PostMapping(value = "/filter",params="action=Search")
    public String inSensitiveSearch(@ModelAttribute Picture picture, Model model) {
        if (!picture.getStringTags().isEmpty() & picture.getText().isEmpty()) {
            Iterable<Picture> pictures = pictureService.filterByOneTagLeast(picture.getStringTags());
            model.addAttribute("pictures", pictures);

        } else {
            Iterable<Picture> pictures = pictureService.inSensitiveSearch(picture.getText().toLowerCase());
            model.addAttribute("pictures", pictures);
        }

        return "/gallery";
    }

    @GetMapping(value = "/filter/{name}")
    public String filterbyTag(@PathVariable("name") String tag_name, @ModelAttribute Picture picture, Model model) {
        Iterable<Picture> pictures = pictureService.filterByOneTagLeast(tag_name);
        model.addAttribute("pictures", pictures);
        return "/gallery";
    }

    @GetMapping(value = "/getImage/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] loadImage(@PathVariable Long id) {
        return pictureService.getImageById(id);
    }

    @GetMapping(value = "/openFull/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] loadFullImage(@PathVariable Long id) {
        return pictureService.getFullImageById(id);
    }

    public String gallery(@PathVariable("id") Long id, Model model) {
        Optional<Picture> picture = pictureService.findPictureById(id);
        model.addAttribute("picture", picture);
        return "/";
    }

}
