package org.gym.Services;

import org.gym.Configuration.PasswordGenerator;
import org.gym.Entities.Trainee;
import org.gym.Persistence.DAOTraineeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TraineeService {
    private final DAOTraineeImpl daoTrainee;
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    @Autowired
    public TraineeService(DAOTraineeImpl daoTrainee){
        this.daoTrainee = daoTrainee;
    }

    public Trainee createTrainee(Long id, String firstname, String lastname, String isActive, Integer yearOfBirth, Integer monthOfBirth, Integer dayOfBirth, String address){
        Trainee trainee = new Trainee();
        try {
            if (id == null) {
                throw new Exception("ID cannot be empty or null");
            }

            if (firstname == null || firstname.trim().isEmpty()) {
                throw new Exception("first name cannot be empty");
            }
            if (lastname == null || lastname.trim().isEmpty()) {
                throw new Exception("last name cannot be empty");
            }
            if (isActive == null || isActive.trim().isEmpty()) {
                trainee.setIsActive(Boolean.FALSE);
            }

            if (yearOfBirth == null ||  monthOfBirth == null || dayOfBirth == null) {
                throw new Exception("Birth fields cannot be empty");
            }

            trainee.setId(id);
            trainee.setFirstName(firstname);
            trainee.setLastName(lastname);

            Integer timesUsernameExist = daoTrainee.validateUsername(firstname + "." + lastname);
            if (timesUsernameExist > 0){
                trainee.setUsername(firstname + "." + lastname + timesUsernameExist);
            }else{
                trainee.setUsername(firstname + "." + lastname);
            }


            String generatedPassword = PasswordGenerator.generateRandomPassword();
            trainee.setPassword(generatedPassword);

            trainee.setIsActive(Boolean.valueOf(isActive));
            trainee.setDateOfBirth(LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth));
            trainee.setAddress(address);

        } catch (Exception e){
            logger.error("Failed to create Trainee", e);
            System.out.println(e.getMessage());
            return null;
        }
        daoTrainee.create(trainee);
        logger.info("Trainee created successfully: {}", trainee);
        return trainee;
    }

    public Trainee updateTrainee(Long id, String isActive, String address){
        try{
            if (id == null){
                throw new Exception("ID cannot be empty or null");
            }
            Optional<Trainee> response = Optional.ofNullable(daoTrainee.getTraineeById(id));
            if(response.isPresent()){
                Trainee trainee = response.get();
                if(isActive != null && !isActive.trim().isEmpty()){
                    trainee.setIsActive(Boolean.parseBoolean(isActive));
                }
                if(address != null && !address.trim().isEmpty()){
                    trainee.setAddress(address);
                }
                daoTrainee.update(trainee);
                logger.info("Trainee updated successfully: {}", trainee);
                return trainee;
            }
        }catch (Exception e){
            logger.error("Failed to update Trainee", e);
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    public void deleteTrainee(Long id){
        try{
            if (id == null){
                throw new Exception("ID cannot be empty or null");
            }
            daoTrainee.delete(selectTrainee(id));
            logger.info("Trainee deleted successfully: {}", id);
        }catch (Exception e){
            logger.error("Failed to delete Trainee", e);
            System.out.println(e.getMessage());
        }
    }

    public Trainee selectTrainee(Long id){
        try{
            if (id == null){
                throw new Exception("ID cannot be empty or null");
            }

            return daoTrainee.getTraineeById(id);

        }catch (Exception e){
            logger.error("Failed to select Trainee", e);
            System.out.println(e.getMessage());
            return null;
        }
    }
}
