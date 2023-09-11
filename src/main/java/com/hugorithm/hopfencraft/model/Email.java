package com.hugorithm.hopfencraft.model;

import com.hugorithm.hopfencraft.enums.EmailType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
@NoArgsConstructor
@Data
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long emailId;
    private LocalDateTime emailSendDate;
    @ManyToOne
    private ApplicationUser user;

    @Enumerated(EnumType.STRING)
    private EmailType emailType;

    public Email(EmailType emailType, LocalDateTime emailSendDate, ApplicationUser user) {
        this.emailType = emailType;
        this.emailSendDate = emailSendDate;
        this.user = user;
    }
}
