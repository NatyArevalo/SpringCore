package org.gym;


import org.gym.Entities.Trainer;
import org.gym.Enumerators.TrainingType;
import org.gym.Persistence.DAOTrainerImpl;
import org.gym.Services.TrainerService;
import org.gym.Storage.TrainerStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainerServiceTest {
    private ResourceLoader resourceLoader;
    private TrainerStorage trainerStorage;
    private List<String> usernames;
    private TrainerService trainerService;
    private DAOTrainerImpl daoTrainer;

    @BeforeAll
    public void setUp() {
        trainerStorage = new TrainerStorage(resourceLoader);
        usernames = new ArrayList<>();
        daoTrainer = new DAOTrainerImpl(trainerStorage, usernames);
        trainerService = new TrainerService(daoTrainer);

    }


    @ParameterizedTest
    @MethodSource("provideTrainers")
    public void testCreateTrainer(Long id, String firstName, String lastName, String username, Boolean isActive, TrainingType specialization) {

        Trainer createdTrainer = trainerService.createTrainer(id, firstName, lastName, String.valueOf(isActive), String.valueOf(specialization));
        assertNotNull(createdTrainer);
        assertEquals(id, createdTrainer.getId());
        assertEquals(firstName, createdTrainer.getFirstName());
        assertEquals(lastName, createdTrainer.getLastName());
        assertEquals(username, createdTrainer.getUsername());
        assertEquals(isActive, createdTrainer.getIsActive());
        assertEquals(specialization, createdTrainer.getSpecialization());

    }
    static Stream<Object[]> provideTrainers() {
        return Stream.of(
                new Object[]{Long.valueOf("11045123456"),"John", "Doe", "John.Doe", true, TrainingType.FUNCTIONAL},
                new Object[]{Long.valueOf("11045123457"),"John", "Doe", "John.Doe1", true, TrainingType.STRENGTH},
                new Object[]{Long.valueOf("11045123458"),"John", "Doe", "John.Doe2", true, TrainingType.AGILITY}
        );
    }

    @ParameterizedTest
    @MethodSource("updateTrainers")
    public void testUpdateTrainer(Long id, String firstName, String lastName, String username, Boolean isActive,
                                  TrainingType specialization, String updatedIsActive, String updatedSpecialization){

        Trainer createdTrainer = trainerService.createTrainer(id, firstName, lastName, String.valueOf(isActive), String.valueOf(specialization));
        assertNotNull(createdTrainer);

        Trainer updatedTrainer = trainerService.updateTrainer(id, updatedIsActive, updatedSpecialization);
        assertNotNull(updatedTrainer);
        assertEquals(id, updatedTrainer.getId());

        if (updatedIsActive.isEmpty()){
            assertEquals(isActive, updatedTrainer.getIsActive());
        }else {
            assertEquals(Boolean.valueOf(updatedIsActive), updatedTrainer.getIsActive());
        }
        if (updatedSpecialization.isEmpty()){
            assertEquals(specialization, updatedTrainer.getSpecialization());
        }else {
            assertEquals(TrainingType.valueOf(updatedSpecialization), updatedTrainer.getSpecialization());
        }

        assertEquals(firstName, updatedTrainer.getFirstName());
        assertEquals(lastName, updatedTrainer.getLastName());

    }

    static  Stream<Object[]> updateTrainers(){
        return Stream.of(
                new Object[]{Long.valueOf("11045123459"), "John", "Doe", "John.Doe", true, TrainingType.FUNCTIONAL, "","STRENGTH"},
                new Object[]{Long.valueOf("11045123451"), "Jane", "Doe", "Jane.Doe", false, TrainingType.STRENGTH, "false", ""}
        );
    }

    @ParameterizedTest
    @MethodSource("selectTrainers")
    public void testSelectTrainer(Long id, String firstName, String lastName, String username, Boolean isActive, TrainingType specialization, Long selectedId) {

        Trainer createdTrainer = trainerService.createTrainer(id, firstName, lastName, String.valueOf(isActive), String.valueOf(specialization));
        assertNotNull(createdTrainer);

        Trainer selectedTrainer = trainerService.selectTrainer(selectedId);

        if(selectedId == null){
            assertNull(selectedTrainer);
        } else if (!id.equals(selectedId)) {
            assertNotEquals(createdTrainer, selectedTrainer);
        }else {
            assertEquals(createdTrainer, selectedTrainer);
        }


    }

    static  Stream<Object[]> selectTrainers(){
        return Stream.of(
                new Object[]{Long.valueOf("11045123452"), "John", "Doe", "John.Doe", true, TrainingType.FUNCTIONAL,Long.valueOf("11045123452")},
                new Object[]{Long.valueOf("11045123453"), "Jane", "Doe", "Jane.Doe", false, TrainingType.STRENGTH, Long.valueOf("11045123452")},
                new Object[]{Long.valueOf("11045123454"),"John", "Doe", "John.Doe1", true, TrainingType.AGILITY,null}
        );
    }


}
