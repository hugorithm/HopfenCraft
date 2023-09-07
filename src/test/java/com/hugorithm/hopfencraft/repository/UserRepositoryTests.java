package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveAll_ReturnSavedUser() {

        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);

        //Act
        ApplicationUser savedUser = userRepository.save(user);

        //Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUserId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_GetAll_ReturnMoreThanOneUser() {
        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);
        ApplicationUser user2 = new ApplicationUser("user2", "Password123!", "email2@example.com", roles);

        //Act
        userRepository.save(user);
        userRepository.save(user2);

        List<ApplicationUser> userList = userRepository.findAll();

        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindById_ReturnUser() {
        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);

        userRepository.save(user);

        ApplicationUser repoUser = userRepository.findById(user.getUserId()).get();

        Assertions.assertThat(repoUser).isNotNull();
    }

    @Test
    public void UserRepository_FindByEmail_ReturnUserNotNull() {
        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);

        userRepository.save(user);

        ApplicationUser repoUser = userRepository.findByEmail(user.getEmail()).get();

        Assertions.assertThat(repoUser).isNotNull();
    }

    @Test
    public void UserRepository_UpdateUser_ReturnUserNotNull() {
        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();


        userRepository.save(user);

        ApplicationUser repoUser = userRepository.findById(user.getUserId()).get();
        repoUser.setUsername("user3");
        repoUser.setEmail("email2@example.com");
        repoUser.setPassword("Password123!2");

        ApplicationUser updatedUser = userRepository.save(repoUser);

        Assertions.assertThat(updatedUser.getUsername()).isNotNull();
        Assertions.assertThat(updatedUser.getEmail()).isNotNull();
        Assertions.assertThat(updatedUser.getPassword()).isNotNull();
        Assertions.assertThat(updatedUser.getUsername()).isNotEqualTo(username);
        Assertions.assertThat(updatedUser.getPassword()).isNotEqualTo(password);
        Assertions.assertThat(updatedUser.getEmail()).isNotEqualTo(email);
    }

    @Test
    public void UserRepository_UserDelete_ReturnUserIsEmpty() {
        //Arrange
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles);

        userRepository.save(user);

        userRepository.deleteById(user.getUserId());
        Optional<ApplicationUser> userReturn = userRepository.findById(user.getUserId());

        Assertions.assertThat(userReturn).isEmpty();
    }

}
