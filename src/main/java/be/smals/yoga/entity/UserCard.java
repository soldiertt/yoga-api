package be.smals.yoga.entity;

import be.smals.yoga.model.CardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cards")
@Getter
@Setter
public class UserCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, columnDefinition = "decimal default 120.00")
    private Float price = 120.0f;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Column(nullable = false, updatable = false, columnDefinition = "integer default 10")
    private Integer capacity = 10;

    @ManyToOne
    private YogaUser owner;

    @ManyToMany
    @OrderBy("courseDate ASC")
    private List<Slot> slots;

    @Column(updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createdTime;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedTime;
}
