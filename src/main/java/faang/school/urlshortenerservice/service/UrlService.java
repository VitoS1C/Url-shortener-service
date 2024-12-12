package faang.school.urlshortenerservice.service;

import faang.school.urlshortenerservice.model.dto.UrlDto;
import faang.school.urlshortenerservice.model.entity.Url;
import faang.school.urlshortenerservice.repository.UrlRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final HashCache hashCache;
    private final UrlRepository urlRepository;
    private final RestTemplateBuilder restTemplateBuilder;

    @Transactional
    @CachePut(key = "#urlDto.hash", value = "urls")
    public String createShortUrl(UrlDto urlDto) {
        if (urlRepository.existsByUrl(urlDto.getUrl()))
            throw new EntityExistsException("URL %s already exists".formatted(urlDto.getUrl()));

        String hash = hashCache.getHash();
        Url url = Url.builder()
                .url(urlDto.getUrl())
                .hash(hash)
                .build();
        urlDto.setHash(hash);
        return urlRepository.save(url).getUrl();
    }

    @Cacheable(key = "#hash", value = "urls")
    @Transactional(readOnly = true)
    public String getOriginalUrl(String hash) {
            return urlRepository.findById(hash).orElseThrow(() ->
                    new EntityNotFoundException("URL with hash %s not found".formatted(hash)))
                    .getUrl();
    }
}
