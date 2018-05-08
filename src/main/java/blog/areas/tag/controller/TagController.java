package blog.areas.tag.controller;

import blog.areas.tag.entity.Tag;
import blog.areas.tag.services.TagServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TagController {
    private TagServices tagServices;

    @Autowired
    public TagController(final TagServices tagServices) {
        this.tagServices = tagServices;
    }

    @GetMapping("/tag/{name}")
    public String articlesWithTag(Model model, @PathVariable String name){
        Tag tag = this.tagServices.findTag(name);

        if (tag == null){
            return "redirect:/";
        }

        model.addAttribute("view","tag/articles");
        model.addAttribute("tag",tag);

        return "base-layout";
    }
}
