package tech.itpark.service;

import tech.itpark.dto.wiki.*;
import tech.itpark.model.Wiki;
import tech.itpark.security.Auth;

import java.util.List;

public interface WikiService {
    List<Wiki> getAllWiki();

    WikiSaveResponseDto createWiki(Auth auth, WikiSaveRequestDto dto);

    WikiUpdateResponseDto updateWiki(Auth auth, WikiUpdateRequestDto dto);

    WikiRemoveResponseDto removeByIdWiki(Auth auth, WikiRemoveRequestDto dto);
}
