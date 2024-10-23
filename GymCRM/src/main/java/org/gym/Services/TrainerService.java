package org.gym.Services;

import org.gym.Configuration.PasswordGenerator;
import org.gym.Entities.Trainer;
import org.gym.Enumerators.TrainingType;
import org.gym.Persistence.DAOTrainerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class TrainerService {

    private final DAOTrainerImpl daoTrainer;
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);
    @Autowired
    public TrainerService(DAOTrainerImpl daoTrainer) {
        this.daoTrainer = daoTrainer;
    }

    public Trainer createTrainer(Long id, String firstname, String lastname, String isActive, String specialization){
        Trainer trainer = new Trainer();
        try{
            if (id == null){
                throw new Exception("ID cannot be empty or null");
            }

            if(firstname == null || firstname.trim().isEmpty()){
                throw new Exception("first name cannot be empty");
            }
            if(lastname == null || lastname.trim().isEmpty()){
                throw new Exception("last name cannot be empty");
            }
            if(isActive == null || isActive.trim().isEmpty()){
               trainer.setIsActive(Boolean.FALSE);
            }

            if(specialization == null || specialization.trim().isEmpty()){
                throw new Exception("specialization cannot be empty");
            }
            trainer.setId(id);
            trainer.setFirstName(firstname);
            trainer.setLastName(lastname);

            Integer timesUsernameExist = daoTrainer.validateUsername(firstname + "." + lastname);
            if (timesUsernameExist > 0){
                trainer.setUsername(firstname + "." + lastname + timesUsernameExist);
            }else{
                trainer.setUsername(firstname + "." + lastname);
            }

            String generatedPassword = PasswordGenerator.generateRandomPassword();
            trainer.setPassword(generatedPassword);

            trainer.setIsActive(Boolean.valueOf(isActive));
            trainer.setSpecialization(TrainingType.valueOf(specialization));

        } catch (Exception e) {
            logger.error("Failed to create Trainer", e);
            System.out.println(e.getMessage());
            return null;
        }
        daoTrainer.create(trainer);
        logger.info("Trainer created successfully: {}", trainer);
        return trainer;


    }

    public Trainer updateTrainer(Long id, String isActive, String specialization){
        try{
            if (id == null){
                throw new Exception("ID cannot be empty or null");
            }
            Optional<Trainer> response = Optional.ofNullable(daoTrainer.getTrainerById(id));
            if(response.isPresent()){
                Trainer trainer = response.get();
                if (isActive != null && !isActive.trim().isEmpty()) {
                    trainer.setIsActive(Boolean.parseBoolean(isActive));
                }
                if (specialization != null && !specialization.trim().isEmpty()) {
                    trainer.setSpecialization(TrainingType.valueOf(specialization));
                }
                daoTrainer.update(trainer);
                logger.info("Trainer updated successfully: {}", trainer);
                return trainer;
            }
        }catch (Exception e){
            logger.error("Failed to update Trainer", e);
            System.out.println(e.getMessage());
            return null;
        }

        return null;
    }

    public Trainer selectTrainer(Long id){
        try{
            if (id == null){
                throw new Exception("ID cannot be empty or null");
            }
            return daoTrainer.getTrainerById(id);

        }catch (Exception e){
            logger.error("Failed to select Trainer", e);
            System.out.println(e.getMessage());
            return null;
        }
    }

}
