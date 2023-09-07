package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Email;
import com.hugorithm.hopfencraft.model.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmailRepositoryTests {
    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserRepository userRepository;
    @Test
    public void EmailRepository_SaveAll_ReturnSavedEmail() {

        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);
        Email email = new Email(Email.EmailType.REGISTRATION, LocalDateTime.now(), user);

        //Act
        Email savedEmail = emailRepository.save(email);

        //Assert
        Assertions.assertThat(savedEmail).isNotNull();
        Assertions.assertThat(savedEmail.getEmailId()).isGreaterThan(0);
    }

    @Test
    public void EmailRepository_GetAll_ReturnMoreThanOneEmail() {
        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);
        Email email = new Email(Email.EmailType.REGISTRATION, LocalDateTime.now(), user);
        Email email2 = new Email(Email.EmailType.ORDER, LocalDateTime.now(), user);

        //Act
        userRepository.save(user);

        emailRepository.save(email);
        emailRepository.save(email2);
        List<Email> emailList = emailRepository.findAll();

        //Assert
        Assertions.assertThat(emailList).isNotNull();
        Assertions.assertThat(emailList.size()).isEqualTo(2);
    }

    @Test
    public void EmailRepository_FindById_ReturnEmail() {
        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);
        Email email = new Email(Email.EmailType.REGISTRATION, LocalDateTime.now(), user);

        //Act
        emailRepository.save(email);
        Email repoEmail = emailRepository.findById(email.getEmailId()).get();

        //Assert
        Assertions.assertThat(repoEmail).isNotNull();
    }

    @Test
    public void EmailRepository_UpdateEmail_ReturnEmailNotNull() {
        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);
        ApplicationUser user2 = new ApplicationUser("user2", "Password123!", "email2@example.com", roles);
        Email email = new Email(Email.EmailType.REGISTRATION, LocalDateTime.now(), user);

        //Act
        emailRepository.save(email);

        Email repoEmail = emailRepository.findById(email.getEmailId()).get();
        repoEmail.setEmailType(Email.EmailType.REGISTRATION);
        repoEmail.setEmailSendDate(LocalDateTime.now());
        repoEmail.setUser(user2);

        Email updatedEmail = emailRepository.save(repoEmail);

        //Assert
        Assertions.assertThat(updatedEmail.getEmailType()).isNotNull();
        Assertions.assertThat(updatedEmail.getEmailSendDate()).isNotNull();
        Assertions.assertThat(updatedEmail.getUser()).isNotNull();
    }

    @Test
    public void EmailRepository_EmailDelete_ReturnEmailIsEmpty() {
        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);
        Email email = new Email(Email.EmailType.REGISTRATION, LocalDateTime.now(), user);

        //Act
        emailRepository.save(email);

        emailRepository.deleteById(email.getEmailId());
        Optional<Email> deletedEmail = emailRepository.findById(email.getEmailId());

        //Assert
        Assertions.assertThat(deletedEmail).isEmpty();
    }
}
