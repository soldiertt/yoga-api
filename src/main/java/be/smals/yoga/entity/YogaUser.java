package be.smals.yoga.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class YogaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String userId;

    @Transient
    private String email;
    @Transient
    private String firstName;
    @Transient
    private String lastName;
    @Transient
    private String phone;

    @OneToMany(mappedBy = "owner")
    private List<UserCard> cards;

    @Column(updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createdTime;

    public YogaUser(final String userId) {
        this.userId = userId;
    }
}
