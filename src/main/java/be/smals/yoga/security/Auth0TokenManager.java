package be.smals.yoga.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.ClientCredentialsOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Component;

@Component
public class Auth0TokenManager {
    @Value("${auth0.client-name}")
    private String clientRegistrationName;

    @Value("${spring.security.oauth2.client.registration.auth0.client-id}")
    private String clientId;

    private final DefaultOAuth2AuthorizedClientManager auth0AuthorizedClientManager;

    public Auth0TokenManager(ClientRegistrationRepository clientRegistrationRepository,
                             OAuth2AuthorizedClientRepository authorizedClientRepository,
                             @Value("${auth0.server-url}") final String audience) {
        final var auth0RequestEntityConverter = new Auth0ClientCredentialsGrantRequestConverter(audience);
        final var responseClient = new DefaultClientCredentialsTokenResponseClient();
        responseClient.setRequestEntityConverter(auth0RequestEntityConverter);
        final var clientProvider = new ClientCredentialsOAuth2AuthorizedClientProvider();
        clientProvider.setAccessTokenResponseClient(responseClient);

        auth0AuthorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        auth0AuthorizedClientManager.setAuthorizedClientProvider(clientProvider);
    }

    public String getAccessToken() {
        final var authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(clientRegistrationName)
                .principal(clientId)
                .build();

        final var authorizedClient = auth0AuthorizedClientManager.authorize(authorizeRequest);
        final var accessToken = authorizedClient.getAccessToken();
        return accessToken.getTokenValue();

    }
}
