package org.gym;

import org.gym.Entities.Trainee;
import org.gym.Persistence.DAOTraineeImpl;
import org.gym.Services.TraineeService;
import org.gym.Storage.TraineeStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ResourceLoader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TraineeServiceTest {
    private ResourceLoader resourceLoader;
    private TraineeStorage traineeStorage;
    private List<String> usernames;
    private DAOTraineeImpl daoTrainee;
    private TraineeService traineeService;

    @BeforeAll
    public void setUp() {
        traineeStorage = new TraineeStorage(resourceLoader);
        usernames = new ArrayList<>();
        daoTrainee = new DAOTraineeImpl(traineeStorage, usernames);
        traineeService = new TraineeService(daoTrainee);
    }
    @ParameterizedTest
    @MethodSource("provideTrainees")
    public void testCreateTrainee(Long id, String firstname, String lastname, String username, String isActive, Integer yearOfBirth, Integer monthOfBirth, Integer dayOfBirth, String address) {
        Trainee createdTrainee = traineeService.createTrainee(id, firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
        assertNotNull(createdTrainee);
        assertEquals(id, createdTrainee.getId());
        assertEquals(firstname, createdTrainee.getFirstName());
        assertEquals(lastname, createdTrainee.getLastName());
        assertEquals(username, createdTrainee.getUsername());
        assertEquals(Boolean.valueOf(isActive), createdTrainee.getIsActive());
        assertEquals(LocalDate.of(yearOfBirth,monthOfBirth,dayOfBirth), createdTrainee.getDateOfBirth());
        assertEquals(address, createdTrainee.getAddress());
    }
    static Stream<Object[]> provideTrainees() {
        return Stream.of(
                new Object[]{Long.valueOf("11045123456"),"John", "Doe", "John.Doe", "true", 2000,12,9,"Cartagena, Colombia"},
                new Object[]{Long.valueOf("11045123457"),"John", "Doe", "John.Doe1", "true", 2000,7,30,"Cartagena, Colombia"},
                new Object[]{Long.valueOf("11045123458"),"John", "Doe", "John.Doe2", "true", 2000,5,20,"Cartagena, Colombia"}
        );
    }

    @ParameterizedTest
    @MethodSource("updateTrainees")
    public void testUpdateTrainee(Long id, String firstname, String lastname, String username, String isActive, Integer yearOfBirth,
                                  Integer monthOfBirth, Integer dayOfBirth, String address, String updatedIsActive, String updatedAddress){
        Trainee createdTrainee = traineeService.createTrainee(id, firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
        assertNotNull(createdTrainee);

        Trainee updatedTrainee = traineeService.updateTrainee(id, updatedIsActive, updatedAddress);
        assertNotNull(updatedTrainee);
        assertEquals(id, updatedTrainee.getId());

        if (updatedIsActive.isEmpty()){
            assertEquals(Boolean.valueOf(isActive), updatedTrainee.getIsActive());
        }else {
            assertEquals(Boolean.valueOf(updatedIsActive), updatedTrainee.getIsActive());
        }
        if (updatedAddress.isEmpty()){
            assertEquals(address, updatedTrainee.getAddress());
        }else {
            assertEquals(updatedAddress, updatedTrainee.getAddress());
        }

        assertEquals(firstname, updatedTrainee.getFirstName());
        assertEquals(lastname, updatedTrainee.getLastName());

    }
    static Stream<Object[]> updateTrainees() {
        return Stream.of(
                new Object[]{Long.valueOf("11045123459"),"John", "Doe", "John.Doe", "true", 2000,12,9,"Cartagena, Colombia", "", "Bogota, Colombia"},
                new Object[]{Long.valueOf("11045123451"),"John", "Doe", "John.Doe1", "true", 2000,7,30,"Cartagena, Colombia", "false", ""},
                new Object[]{Long.valueOf("11045123452"),"John", "Doe", "John.Doe2", "true", 2000,5,20,"Cartagena, Colombia", "", ""}
        );
    }
    @ParameterizedTest
    @MethodSource("deleteTrainees")
    public void testUpdateTrainee(Long id, String firstname, String lastname, String username, String isActive, Integer yearOfBirth,
                                  Integer monthOfBirth, Integer dayOfBirth, String address, Long idToDelete){
        Trainee createdTrainee = traineeService.createTrainee(id, firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
        assertNotNull(createdTrainee);
        traineeService.deleteTrainee(idToDelete);
        assertNull(traineeService.selectTrainee(idToDelete));

    }
    static Stream<Object[]> deleteTrainees() {
        return Stream.of(
                new Object[]{Long.valueOf("11045123453"),"John", "Doe", "John.Doe", "true", 2000,12,9,"Cartagena, Colombia", Long.valueOf("11045123453")},
                new Object[]{Long.valueOf("11045123454"),"John", "Doe", "John.Doe1", "true", 2000,7,30,"Cartagena, Colombia", null}
                );
    }

    @ParameterizedTest
    @MethodSource("selectTrainees")
    public void testSelectTrainee(Long id, String firstname, String lastname, String username, String isActive, Integer yearOfBirth,
                                  Integer monthOfBirth, Integer dayOfBirth, String address, Long selectedId){
        Trainee createdTrainee = traineeService.createTrainee(id, firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
        assertNotNull(createdTrainee);
        Trainee selectedTrainee = traineeService.selectTrainee(selectedId);

        if(selectedId == null){
            assertNull(selectedTrainee);
        } else if (!id.equals(selectedId)) {
            assertNotEquals(createdTrainee, selectedTrainee);
        }else {
            assertEquals(createdTrainee, selectedTrainee);
        }
    }
    static Stream<Object[]> selectTrainees() {
        return Stream.of(
                new Object[]{Long.valueOf("11045123453"),"John", "Doe", "John.Doe", "true", 2000,12,9,"Cartagena, Colombia", Long.valueOf("11045123453")},
                new Object[]{Long.valueOf("11045123454"),"John", "Doe", "John.Doe1", "true", 2000,7,30,"Cartagena, Colombia", null}
        );
    }
}

