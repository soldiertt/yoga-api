package be.smals.yoga.controller;

import static be.smals.yoga.model.MailText.SUBJECT_USER_CARD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.smals.yoga.entity.UserCard;
import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.service.MailService;
import be.smals.yoga.service.UserCardService;
import be.smals.yoga.service.UserService;
import jakarta.mail.MessagingException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class PrivateCardApiTest {

    @Mock
    private UserCardService userCardService;

    @Mock
    private UserService userService;

    @Mock
    private MailService mailService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PrivateCardApi privateCardApi;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("auth0|12345");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testCreateMediumCardFormatsCapacityCorrectly() throws MessagingException {
        YogaUser user = new YogaUser();
        user.setEmail("user@example.com");
        user.setCards(new ArrayList<>());

        when(userService.findByUserId("auth0|12345")).thenReturn(user);
        when(userCardService.save(any(UserCard.class))).thenAnswer(invocation -> invocation.getArgument(0));

        privateCardApi.createMedium();

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(mailService).sendSimpleMessage(eq("user@example.com"), eq(SUBJECT_USER_CARD_REQUEST), bodyCaptor.capture());

        String capturedBody = bodyCaptor.getValue();
        assertThat(capturedBody).contains("abonnement 5 séances");
        assertThat(capturedBody).contains("Prix: 80.0€");
    }

    @Test
    void testCreateLongCardFormatsCapacityCorrectly() throws MessagingException {
        YogaUser user = new YogaUser();
        user.setEmail("user@example.com");
        user.setCards(new ArrayList<>());

        when(userService.findByUserId("auth0|12345")).thenReturn(user);
        when(userCardService.save(any(UserCard.class))).thenAnswer(invocation -> invocation.getArgument(0));

        privateCardApi.createLong();

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(mailService).sendSimpleMessage(eq("user@example.com"), eq(SUBJECT_USER_CARD_REQUEST), bodyCaptor.capture());

        String capturedBody = bodyCaptor.getValue();
        assertThat(capturedBody).contains("abonnement 10 séances");
        assertThat(capturedBody).contains("Prix: 160.0€");
    }
}
