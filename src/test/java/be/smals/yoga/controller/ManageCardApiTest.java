package be.smals.yoga.controller;

import static be.smals.yoga.model.MailText.SUBJECT_USER_HAS_CARD_VALIDATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import be.smals.yoga.entity.UserCard;
import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.service.MailService;
import be.smals.yoga.service.UserCardService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ManageCardApiTest {

    @Mock
    private UserCardService userCardService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private ManageCardApi manageCardApi;

    @Test
    void testUpdateFormatsCapacityDynamicallyInEmail() throws MessagingException {
        YogaUser owner = new YogaUser();
        owner.setEmail("student@example.com");

        UserCard existingCard = new UserCard();
        existingCard.setId(1L);
        existingCard.setCapacity(5);
        existingCard.setOwner(owner);

        UserCard updatePayload = new UserCard();
        updatePayload.setId(1L);

        when(userCardService.findById(1L)).thenReturn(existingCard);
        when(userCardService.update(updatePayload)).thenReturn(existingCard);

        UserCard result = manageCardApi.update(1L, updatePayload);

        assertThat(result).isNotNull();

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(mailService).sendSimpleMessage(eq("student@example.com"), eq(SUBJECT_USER_HAS_CARD_VALIDATED), bodyCaptor.capture());

        String capturedBody = bodyCaptor.getValue();
        assertThat(capturedBody).contains("votre carte d'abonnement 5 séances");
        assertThat(capturedBody).contains("profiter de 5 séances à réserver");
    }

    @Test
    void testUpdateWithDifferentIdDoesNotSendMail() throws MessagingException {
        UserCard updatePayload = new UserCard();
        updatePayload.setId(2L);

        UserCard result = manageCardApi.update(1L, updatePayload);

        assertThat(result).isNull();
        verifyNoInteractions(mailService);
    }
}
