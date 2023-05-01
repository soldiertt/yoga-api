package be.smals.yoga.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SlotBooking {
    private Long slotId;
    private Boolean emailConfirmation;
}
