package tech.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.itpark.dto.wiki.*;
import tech.itpark.exception.PermissionDeniedException;

import tech.itpark.model.Wiki;
import tech.itpark.repository.WikiRepository;
import tech.itpark.security.Auth;

import java.util.List;



@Service
@RequiredArgsConstructor
public class WikiServiceImpl implements WikiService {
    private final WikiRepository repository;

    @Override
    public List<Wiki> getAllWiki() {
        return repository.getAll();
    }

    @Override
    public WikiSaveResponseDto createWiki(Auth auth, WikiSaveRequestDto dto) {
        if (auth.isAnonymous()) {
            throw new PermissionDeniedException();
        }
        final var saved = repository.create(new Wiki(
                dto.getId(),
                auth.getId(),
                dto.getAuthorId(),
                dto.getPerson()
        ));
        return new WikiSaveResponseDto(saved.getId(), saved.getAuthorId(), saved.getPerson());
    }

    @Override
    public WikiUpdateResponseDto updateWiki(Auth auth, WikiUpdateRequestDto dto) {
        if (!auth.hasAnyRole("ROLE_ADMIN", "ROLE_MODERATOR")) {
            throw new PermissionDeniedException();
        }
        final var update = repository.update(new Wiki(
                dto.getId(),
                dto.getAuthorId(),
                dto.getPerson(),
                dto.getContent()
        ));
        return new WikiUpdateResponseDto(update.getId(), update.getAuthorId(), update.getPerson(), update.getContent());
    }

    @Override
    public WikiRemoveResponseDto removeByIdWiki(Auth auth, WikiRemoveRequestDto dto) {
       final var removedId = dto.getId();
        if (auth.isAnonymous()) {
            throw new PermissionDeniedException();
        }
        repository.remove(removedId, true);
        return new WikiRemoveResponseDto(dto.getId());
    }


}
