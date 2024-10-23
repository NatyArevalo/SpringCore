package org.gym.Services;

import org.gym.Entities.Training;
import org.gym.Enumerators.TrainingType;
import org.gym.Persistence.DAOTraineeImpl;
import org.gym.Persistence.DAOTrainerImpl;
import org.gym.Persistence.DAOTrainingImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class TrainingService {
    private final DAOTrainingImpl daoTraining;
    private final DAOTraineeImpl daoTrainee;
    private final DAOTrainerImpl daoTrainer;
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    @Autowired
    public TrainingService(DAOTrainingImpl daoTraining, DAOTraineeImpl daoTrainee, DAOTrainerImpl daoTrainer){
        this.daoTraining = daoTraining;
        this.daoTrainee = daoTrainee;
        this.daoTrainer = daoTrainer;
    }

    public Training createTraining(Long traineeId, Long trainerId, String trainingName, String specialization, Integer yearOfTraning, Integer monthOfTraining, Integer dayOfTraining, Double duration){
        Training training = new Training();
        try {
            if (trainerId == null) {
                throw new Exception("ID of Trainer cannot be empty or null");
            }

            if (traineeId == null) {
                throw new Exception("ID of Trainee cannot be empty or null");
            }

            if (trainingName == null || trainingName.trim().isEmpty()) {
                throw new Exception("Name of training cannot be empty");
            }
            if (specialization == null || specialization.trim().isEmpty()) {
                throw new Exception("Training type name cannot be empty");
            }
            if (yearOfTraning == null || monthOfTraining == null || dayOfTraining == null) {
                throw new Exception("Training Date fields cannot be empty");
            }
            if (duration == null) {
                throw new Exception("Duration of Training cannot be empty or null");
            }
            training.setId(UUID.randomUUID().toString());
            if(daoTrainer.getTrainerById(trainerId)==null){
                throw new Exception("ID of Trainer needs to be valid");
            }else{
                training.setTrainerId(trainerId);
            }
            if(daoTrainee.getTraineeById(traineeId)==null){
                throw new Exception("ID of Trainee needs to be valid");
            }else{
                training.setTraineeId(traineeId);
            }

            training.setTrainingName(trainingName);
            training.setSpecialization(TrainingType.valueOf(specialization));
            training.setTrainingDate(LocalDate.of(yearOfTraning, monthOfTraining, dayOfTraining));
            training.setDuration(duration);
        } catch (Exception e){
            logger.error("Failed to create Training", e);
            System.out.println(e.getMessage());
            return null;
        }
        daoTraining.create(training);
        logger.info("Training created successfully: {}", training);
        return training;
    }

    public Training selectTraining(String id){
        try{
            if (id == null || id.trim().isEmpty()){
                throw new Exception("ID cannot be empty or null");
            }
            return daoTraining.getTrainingById(id);

        }catch (Exception e){
            logger.error("Failed to select Training", e);
            System.out.println(e.getMessage());
            return null;
        }
    }

}
