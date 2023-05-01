package be.smals.yoga.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "slots")
@Getter
@Setter
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate courseDate;
    private String courseTime;

    @Transient
    private Integer participantsCount;

    @ManyToMany(mappedBy = "slots")
    private List<UserCard> cards;

    @Column(updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createdTime;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedTime;
}
