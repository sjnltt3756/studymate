package com.studymate.service;

import com.studymate.dto.tag.TagRequest;
import com.studymate.entity.Tag;
import com.studymate.entity.User;
import com.studymate.repository.TagRepository;
import com.studymate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public void createTag(String username, TagRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Tag tag = Tag.create(request.getName(), user);
        tagRepository.save(tag);
    }

    public List<String> getTags(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return tagRepository.findAllByUserId(user.getId())
                .stream().map(Tag::getName).toList();
    }

    public void deleteTag(String username, Long tagId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("태그 없음"));

        if (!tag.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 태그만 삭제할 수 있습니다.");
        }

        tagRepository.delete(tag);
    }
}
