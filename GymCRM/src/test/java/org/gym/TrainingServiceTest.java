package org.gym;


import org.gym.Entities.Trainee;
import org.gym.Entities.Trainer;
import org.gym.Entities.Training;
import org.gym.Enumerators.TrainingType;
import org.gym.Persistence.DAOTraineeImpl;
import org.gym.Persistence.DAOTrainerImpl;
import org.gym.Persistence.DAOTrainingImpl;
import org.gym.Services.TraineeService;
import org.gym.Services.TrainerService;
import org.gym.Services.TrainingService;
import org.gym.Storage.TraineeStorage;
import org.gym.Storage.TrainerStorage;
import org.gym.Storage.TrainingStorage;
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
public class TrainingServiceTest {
    private ResourceLoader resourceLoader;
    private TrainingStorage trainingStorage;
    private DAOTrainingImpl daoTraining;
    private TraineeStorage traineeStorage;
    private DAOTraineeImpl daoTrainee;
    private List<String> usernames;
    private TrainerStorage trainerStorage;
    private DAOTrainerImpl daoTrainer;
    private TrainingService trainingService;
    private TraineeService traineeService;
    private TrainerService trainerService;

    @BeforeAll
    public void setUp() {
        usernames = new ArrayList<>();
        traineeStorage = new TraineeStorage(resourceLoader);
        daoTrainee = new DAOTraineeImpl(traineeStorage, usernames);
        traineeService = new TraineeService(daoTrainee);
        trainerStorage = new TrainerStorage(resourceLoader);
        daoTrainer = new DAOTrainerImpl(trainerStorage, usernames);
        trainerService = new TrainerService(daoTrainer);
        trainingStorage = new TrainingStorage(resourceLoader);
        daoTraining = new DAOTrainingImpl(trainingStorage);
        trainingService = new TrainingService(daoTraining, daoTrainee, daoTrainer);
    }

    @ParameterizedTest
    @MethodSource("provideTraining")
    public void testCreateTraining(Long traineeId, String traineeFirstname, String traineeLastname, String traineeUsername, String traineeIsActive, Integer traineeYearOfBirth, Integer traineeMonthOfBirth, Integer traineeDayOfBirth, String traineeAddress,
                                   Long trainerId, String trainerFirstName, String trainerLastName, String trainerUsername, Boolean trainerIsActive, TrainingType trainerSpecialization,
                                   Long traineeIdforTraining, Long trainerIdforTraining, String trainingName, String trainingSpecialization, Integer yearOfTraning, Integer monthOfTraining, Integer dayOfTraining, Double durationOfTraining) {

        Trainee createdTrainee = traineeService.createTrainee(traineeId, traineeFirstname, traineeLastname, traineeIsActive, traineeYearOfBirth, traineeMonthOfBirth, traineeDayOfBirth, traineeAddress);
        assertNotNull(createdTrainee);

        Trainer createdTrainer = trainerService.createTrainer(trainerId, trainerFirstName, trainerLastName, String.valueOf(trainerIsActive), String.valueOf(trainerSpecialization));
        assertNotNull(createdTrainer);

        if (traineeId.equals(traineeIdforTraining) && trainerId.equals(trainerIdforTraining)) {
            Training createdTraining = trainingService.createTraining(traineeIdforTraining, trainerIdforTraining, trainingName, trainingSpecialization, yearOfTraning, monthOfTraining, dayOfTraining, durationOfTraining);
            assertNotNull(createdTraining);
            assertEquals(trainingName, createdTraining.getTrainingName());
            assertEquals(TrainingType.valueOf(trainingSpecialization), createdTraining.getSpecialization());
            assertEquals(LocalDate.of(yearOfTraning, monthOfTraining, dayOfTraining), createdTraining.getTrainingDate());
            assertEquals(durationOfTraining, createdTraining.getDuration());
        } else {
            Training createdTraining = trainingService.createTraining(traineeIdforTraining, trainerIdforTraining, trainingName, trainingSpecialization, yearOfTraning, monthOfTraining, dayOfTraining, durationOfTraining);
            assertNull(createdTraining);
        }


    }

    static Stream<Object[]> provideTraining() {
        return Stream.of(
                new Object[]{Long.valueOf("11045123456"), "John", "Doe", "John.Doe", "true", 2000, 12, 9, "Cartagena, Colombia",
                        Long.valueOf("11045123457"), "Jane", "Doe", "John.Doe", true, TrainingType.FUNCTIONAL,
                        Long.valueOf("11045123456"), Long.valueOf("11045123457"), "Leg day", "FUNCTIONAL", 2024, 10, 15, 1.5},
                new Object[]{Long.valueOf("11045123458"), "John", "Doe", "John.Doe", "true", 2000, 12, 9, "Cartagena, Colombia",
                        Long.valueOf("11045123459"), "Jane", "Doe", "John.Doe", true, TrainingType.FUNCTIONAL,
                        Long.valueOf("11045123451"), Long.valueOf("11045123457"), "Leg day", "FUNCTIONAL", 2024, 10, 15, 1.5}
        );
    }

    @ParameterizedTest
    @MethodSource("selectTraining")
    public void testSelectTraining(Long traineeId, String traineeFirstname, String traineeLastname, String traineeUsername, String traineeIsActive, Integer traineeYearOfBirth, Integer traineeMonthOfBirth, Integer traineeDayOfBirth, String traineeAddress,
                                   Long trainerId, String trainerFirstName, String trainerLastName, String trainerUsername, Boolean trainerIsActive, TrainingType trainerSpecialization,
                                   Long traineeIdforTraining, Long trainerIdforTraining, String trainingName, String trainingSpecialization, Integer yearOfTraning, Integer monthOfTraining, Integer dayOfTraining, Double durationOfTraining,
                                   String selectedTrainingId) {

        Trainee createdTrainee = traineeService.createTrainee(traineeId, traineeFirstname, traineeLastname, traineeIsActive, traineeYearOfBirth, traineeMonthOfBirth, traineeDayOfBirth, traineeAddress);
        assertNotNull(createdTrainee);

        Trainer createdTrainer = trainerService.createTrainer(trainerId, trainerFirstName, trainerLastName, String.valueOf(trainerIsActive), String.valueOf(trainerSpecialization));
        assertNotNull(createdTrainer);

        Training createdTraining = trainingService.createTraining(traineeIdforTraining, trainerIdforTraining, trainingName, trainingSpecialization, yearOfTraning, monthOfTraining, dayOfTraining, durationOfTraining);
        assertNotNull(createdTraining);

        if(selectedTrainingId == null){
            Training selectedTraining = trainingService.selectTraining(null);
            assertNull(selectedTraining);
        }else{
            Training selectedTraining = trainingService.selectTraining(createdTraining.getId());
            assertEquals(createdTraining, selectedTraining);
        }

    }
    static Stream<Object[]> selectTraining() {
        return Stream.of(
                new Object[]{Long.valueOf("11045123456"), "John", "Doe", "John.Doe", "true", 2000, 12, 9, "Cartagena, Colombia",
                        Long.valueOf("11045123457"),"Jane", "Doe", "John.Doe", true, TrainingType.FUNCTIONAL,
                        Long.valueOf("11045123456"), Long.valueOf("11045123457"), "Leg day", "FUNCTIONAL", 2024,10,15,1.5, "generatedID"},
                new Object[]{Long.valueOf("11045123458"), "John", "Doe", "John.Doe", "true", 2000, 12, 9, "Cartagena, Colombia",
                        Long.valueOf("11045123459"),"Jane", "Doe", "John.Doe", true, TrainingType.FUNCTIONAL,
                        Long.valueOf("11045123458"), Long.valueOf("11045123459"), "Leg day", "FUNCTIONAL", 2024,10,15,1.5, null}
        );
    }
}