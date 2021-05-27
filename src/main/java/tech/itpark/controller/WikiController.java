package tech.itpark.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import tech.itpark.bodyconverter.BodyConverter;
import tech.itpark.dto.wiki.WikiRemoveRequestDto;
import tech.itpark.dto.wiki.WikiSaveRequestDto;
import tech.itpark.dto.wiki.WikiUpdateRequestDto;
import tech.itpark.http.ContentTypes;
import tech.itpark.security.HttpServletRequestAuthToken;
import tech.itpark.service.WikiService;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WikiController {
    private final WikiService service;
    private final List<BodyConverter> converters;

    public void getAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var responseDto = service.getAllWiki();
        write(responseDto, ContentTypes.APPLICATION_JSON, response);
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var auth = HttpServletRequestAuthToken.auth(request);
        final var requestDto = read(WikiSaveRequestDto.class, request);
        final var responseDto = service.createWiki(auth, requestDto);
        write(responseDto, ContentTypes.APPLICATION_JSON, response);
    }

    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var auth = HttpServletRequestAuthToken.auth(request);
        final var requestDto = read(WikiUpdateRequestDto.class, request);
        final var responseDto = service.updateWiki(auth, requestDto);
        write(responseDto, ContentTypes.APPLICATION_JSON, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) {
        final var auth = HttpServletRequestAuthToken.auth(request);
        final var requestDto = read(WikiRemoveRequestDto.class, request);
        final var responseDto = service.removeByIdWiki(auth, requestDto);
        write(responseDto, ContentTypes.APPLICATION_JSON, response);
    }

    public <T> T read(Class<T> clazz, HttpServletRequest request) {
        for (final var converter : converters) {
            if (!converter.canRead(request.getContentType(), clazz)) {
                continue;
            }

            try {
                return converter.read(request.getReader(), clazz);
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: convert to special exception
                throw new RuntimeException(e);
            }
        }
        // TODO: convert to special exception
        throw new RuntimeException("no converters support given content type");
    }

    private void write(Object data, String contentType, HttpServletResponse response) {
        for (final var converter : converters) {
            if (!converter.canWrite(contentType, data.getClass())) {
                continue;
            }

            try {
                response.setContentType(contentType);
                converter.write(response.getWriter(), data);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: convert to special exception
                throw new RuntimeException(e);
            }
        }
        // TODO: convert to special exception
        throw new RuntimeException("no converters support given content type");
    }






}
