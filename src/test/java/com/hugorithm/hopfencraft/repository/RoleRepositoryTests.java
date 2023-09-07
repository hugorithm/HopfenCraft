package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.model.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void RoleRepository_SaveAll_ReturnSavedRole() {

        //Arrange
        Role role = new Role("USER");

        //Act
        Role savedRole = roleRepository.save(role);

        //Assert
        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getRoleId()).isGreaterThan(0);
    }

    @Test
    public void RoleRepository_GetAll_ReturnMoreThanOneRole() {
        //Arrange
        Role role = new Role("USER");
        Role role2 = new Role("ADMIN");

        //Act
        roleRepository.save(role);
        roleRepository.save(role2);
        List<Role> roleList = roleRepository.findAll();

        //Assert
        Assertions.assertThat(roleList).isNotNull();
        Assertions.assertThat(roleList.size()).isEqualTo(2);
    }

    @Test
    public void RoleRepository_FindById_ReturnRole() {
        //Arrange
        Role role = new Role("USER");

        //Act
        roleRepository.save(role);
        Role repoRole = roleRepository.findById(role.getRoleId()).get();

        //Assert
        Assertions.assertThat(repoRole).isNotNull();
    }

    @Test
    public void RoleRepository_FindByAuthority_ReturnRoleNotNull() {
        //Arrange
        Role role = new Role("USER");

        //Act
        roleRepository.save(role);
        Role repoRole = roleRepository.findByAuthority(role.getAuthority()).get();

        Assertions.assertThat(repoRole).isNotNull();
    }

    @Test
    public void RoleRepository_UpdateRole_ReturnRoleNotNull() {
        //Arrange
        Role role = new Role("USER");

        //Act
        roleRepository.save(role);

        Role repoRole = roleRepository.findById(role.getRoleId()).get();
        repoRole.setAuthority("ADMIN");

        Role updatedRole = roleRepository.save(repoRole);

        //Assert
        Assertions.assertThat(updatedRole.getAuthority()).isNotNull();
        Assertions.assertThat(updatedRole.getRoleId()).isNotNull();
    }

    @Test
    public void RoleRepository_RoleDelete_ReturnRoleIsEmpty() {
        //Arrange
        Role role = new Role("USER");

        //Act
        roleRepository.save(role);

        roleRepository.deleteById(role.getRoleId());
        Optional<Role> deletedRole = roleRepository.findById(role.getRoleId());

        //Assert
        Assertions.assertThat(deletedRole).isEmpty();
    }
}
