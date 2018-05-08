package blog.areas.tag.services;

import blog.areas.tag.entity.Tag;
import blog.areas.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServicesImpl implements TagServices{
    private TagRepository tagRepository;

    @Autowired
    public TagServicesImpl(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag findTag(String name) {
        return this.tagRepository.findByName(name);
    }
}
