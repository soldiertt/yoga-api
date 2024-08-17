package be.smals.yoga.model;

public interface MailText {

    String SUBJECT_USER_HAS_CARD_VALIDATED = "YogaenPevele.fr - Carte validée";
    String BODY_USER_HAS_CARD_VALIDATED = """
                    <p>Bonjour,</p>
                    <p>Nous venons de valider votre carte d'abonnement 10 séances à YogaEnPevele.fr<p>
                    <p>Vous pouvez désormais profiter de 10 séances à réserver en ligne dans la zone "Abonnement".</p>
                    <p>Yoga En Pévèle - <a href="https://www.yogaenpevele.fr">https://www.yogaenpevele.fr</a></p>
                    """;
    String BODY_USER_CARD_REQUEST = """
            <p>Bonjour,</p>
            <p>Nous avons bien reçu votre demande de carte pour un abonnement 10 séances.<br/>
            Nous vous remercions pour votre fidelité.
            </p>
            <p>Prix: %s€</p>
            <p>Expiration: %s</p>
            <p>Après avoir commandé cette carte, veuillez payer le montant à l'aide d'un virement bancaire
            avec les références suivantes:</p>
            <p>
                Nom: Yoga En Pévèle<br/>
                N° de compte : FR4545 4545 4512 2585<br/>
                Communication: Votre nom + prénom
            </p>
            <p>Votre demande sera traitée dès que possible, après réception du paiement (compter 3 jours ouvrables),
            vous serez alors en mesure de faire vos réservations de séances en ligne.</p>
            <p>Yoga En Pévèle - <a href="https://www.yogaenpevele.fr">https://www.yogaenpevele.fr</a></p>""";
    String SUBJECT_USER_CARD_REQUEST = "YogaEnPevele.fr - votre carte";
    String BODY_ADMIN_CARD_REQUEST = """
            <p>Une demande de carte a été introduite.</p>
            <p>Yoga En Pévèle - <a href="https://www.yogaenpevele.fr">https://www.yogaenpevele.fr</a></p>""";
    String SUBJECT_ADMIN_CARD_REQUEST = "YogaEnPevele.fr - Demande de carte";
    String BODY_ADMIN_SLOT_BOOKING = """
            <p>Une nouvelle réservation a été introduite.</p>
            <p>Yoga En Pévèle - <a href="https://www.yogaenpevele.fr">https://www.yogaenpevele.fr</a></p>""";
    String SUBJECT_ADMIN_SLOT_BOOKING = "YogaEnPevele.fr - Réservation ";
    String BODY_USER_SLOT_BOOKING = """
                    <p>Bonjour,</p>
                    <p>Ce mail est une confirmation de votre réservation à une séance de yoga à YogaEnPevele.fr<p>
                    <p>Détails:</p>
                    <ul>
                        <li>Date: %s</li>
                        <li>Heure: %s</li>
                    </ul>
                    <p>Yoga En Pévèle - <a href="https://www.yogaenpevele.fr">https://www.yogaenpevele.fr</a></p>
                    """;
    String SUBJECT_USER_SLOT_BOOKING = "YogaenPevele.fr - Votre réservation";
    String SUBJECT_ADMIN_SLOT_CANCELLED = "YogaEnPevele.fr - Réservation annulée";
    String BODY_ADMIN_SLOT_CANCELLED = """
            <p>Une réservation a été annulée.</p>
            <p>Yoga En Pévèle - <a href="https://www.yogaenpevele.fr">https://www.yogaenpevele.fr</a></p>""";
}
