package be.smals.yoga.repository;

import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.model.Auth0User;
import be.smals.yoga.security.Auth0TokenManager;
import org.apache.hc.core5.http.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Auth0Client {
    private final String resourceServerURL;
    private final Auth0TokenManager tokenManager;
    private final RestTemplate restTemplate;

    public Auth0Client(@Value("${auth0.server-url}") final String resourceServerURL,
                       final Auth0TokenManager tokenManager,
                       final RestTemplateBuilder restTemplateBuilder) {
        this.resourceServerURL = resourceServerURL;
        this.tokenManager = tokenManager;
        this.restTemplate = restTemplateBuilder.build();
        this.restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // Support patch requests
    }

    public Auth0User userInfo(final String userId) {
        final var token = tokenManager.getAccessToken();
        final var url = resourceServerURL + "/users/" + userId;
        final var request = new HttpEntity<>(getHttpHeaders(token));
        final var response = restTemplate.exchange(url, HttpMethod.GET, request, Auth0User.class);
        return response.getBody();
    }

    public void updateUser(final String userId, final YogaUser user) {
        final var userUpdatedInfo = new Auth0User();
        final var userMetadata = new Auth0User.UserMetadata(user.getFirstName(), user.getLastName(), user.getPhone());
        userUpdatedInfo.setUser_metadata(userMetadata);
        final var token = tokenManager.getAccessToken();
        final var url = resourceServerURL + "/users/" + userId;
        final var request = new HttpEntity<>(userUpdatedInfo, getHttpHeaders(token));
        restTemplate.exchange(url, HttpMethod.PATCH, request, Auth0User.class);
    }

    private static HttpHeaders getHttpHeaders(String token) {
        final var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        return headers;
    }
}
