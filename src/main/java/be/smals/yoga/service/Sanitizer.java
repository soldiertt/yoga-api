package be.smals.yoga.service;

import be.smals.yoga.entity.Slot;
import be.smals.yoga.entity.YogaUser;
import be.smals.yoga.entity.UserCard;

import java.util.List;

public class Sanitizer {

    public static List<Slot> forPublicSlots(final List<Slot> slots) {
        slots.forEach(slot -> {
            slot.setParticipantsCount(slot.getCards().size());
            slot.setCards(null);
        });
        return slots;
    }

    public static UserCard forPrivateCard(final UserCard card) {
        card.setOwner(null);
        if (card.getSlots() != null) {
            card.getSlots().forEach(cardSlot -> {
                cardSlot.setCards(null);
            });
        }
        return card;
    }

    public static YogaUser forPrivateUser(final YogaUser user) {
        if (user.getCards() != null) {
            user.getCards().forEach(Sanitizer::forPrivateCard);
        }
        return user;
    }

    public static List<UserCard> forManageCards(final List<UserCard> cards) {
        cards.forEach(Sanitizer::forManageCard);
        return cards;
    }

    public static UserCard forManageCard(final UserCard card) {
        card.getOwner().setCards(null);
        if (card.getSlots() != null) {
            card.getSlots().forEach(slot -> {
                slot.setCards(null);
            });
        }
        return card;
    }

    public static List<Slot> forManageSlots(final List<Slot> slots) {
        slots.forEach(slot -> {
            if (slot.getCards() != null) {
                slot.getCards().forEach(card -> {
                    card.getOwner().setCards(null);
                    card.setSlots(null);
                });
            }
        });
        return slots;
    }

    public static List<YogaUser> forManageUsers(final List<YogaUser> users) {
        users.forEach(user -> {
            if (user.getCards() != null) {
                user.getCards().forEach(Sanitizer::forManageCard);
            }
        });
        return users;
    }
}
