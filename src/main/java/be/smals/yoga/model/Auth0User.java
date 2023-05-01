package be.smals.yoga.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Auth0User {
    private String name;
    private String family_name;
    private String given_name;
    private String user_id;
    private String email;
    private Boolean email_verified;
    private String phone_number;
    private String picture;
    private String updated_at;
    private String created_at;
    private UserMetadata user_metadata;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    public static class UserMetadata {
        private String first_name;
        private String last_name;
        private String phone;
    }



}

