package org.gym.Configuration;

import org.gym.Entities.Trainee;
import org.gym.Entities.Trainer;
import org.gym.Entities.Training;
import org.gym.Services.TraineeService;
import org.gym.Services.TrainerService;
import org.gym.Services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GymFacade {
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    public Trainer createTrainer(Long id, String firstName, String lastName, String isActive, String specialization) {
        return trainerService.createTrainer(id, firstName, lastName, isActive, specialization);
    }

    public Trainer updateTrainer(Long id, String isActive, String specialization) {
        return trainerService.updateTrainer(id, isActive, specialization);
    }

    public Trainer selectTrainer(Long id) {
        return trainerService.selectTrainer(id);
    }

    public Trainee createTrainee(Long id, String firstname, String lastname, String isActive, Integer yearOfBirth, Integer monthOfBirth, Integer dayOfBirth, String address) {
        return traineeService.createTrainee(id, firstname, lastname, isActive, yearOfBirth, monthOfBirth, dayOfBirth, address);
    }

    public Trainee updateTrainee(Long id, String isActive, String address) {
        return traineeService.updateTrainee(id, isActive, address);
    }

    public void deleteTrainee(Long id){
        traineeService.deleteTrainee(id);
    }

    public Trainee getTrainee(Long id) {
        return traineeService.selectTrainee(id);
    }

    public Training createTraining(Long traineeId, Long trainerId, String trainingName, String specialization, Integer yearOfTraning, Integer monthOfTraining, Integer dayOfTraining, Double duration) {
        return trainingService.createTraining(traineeId, trainerId, trainingName, specialization, yearOfTraning, monthOfTraining, dayOfTraining, duration);
    }

    public Training selectTraining(String id) {
        return trainingService.selectTraining(id);
    }
}
