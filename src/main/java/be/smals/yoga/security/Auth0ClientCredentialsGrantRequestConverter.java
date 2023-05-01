package be.smals.yoga.security;

import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

public class Auth0ClientCredentialsGrantRequestConverter extends OAuth2ClientCredentialsGrantRequestEntityConverter {

    private final String audience;

    public Auth0ClientCredentialsGrantRequestConverter(String audience) {
        this.audience = audience;
    }

    @Override
    protected MultiValueMap<String, String> createParameters(OAuth2ClientCredentialsGrantRequest clientCredentialsGrantRequest) {
        final var parameters = super.createParameters(clientCredentialsGrantRequest);
        parameters.add("audience", audience);
        return parameters;
    }
}
