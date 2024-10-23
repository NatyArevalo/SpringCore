package org.gym.Persistence;

import org.gym.DAO.TraineeDAO;
import org.gym.Entities.Trainee;
import org.gym.Storage.TraineeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DAOTraineeImpl implements TraineeDAO {
    private final Map<Long, Trainee> traineeMap;
    private final List<String> usernames;
    @Autowired
    public DAOTraineeImpl (TraineeStorage traineeStorage, List<String> usernames) {
        this.traineeMap = traineeStorage.getTraineeMap();
        this.usernames = usernames;
    }
    public List<Trainee> getAll() {
        return new ArrayList<Trainee>(traineeMap.values());
    }
    public Trainee getTraineeById(Long userId) {
        return traineeMap.get(userId);
    }
    public void create(Trainee trainee) {
        traineeMap.put(trainee.getId(), trainee);
        usernames.add(trainee.getFirstName() + "." + trainee.getLastName());
    }

    public Integer validateUsername(String username){
        Integer timesUsernameExist = 0;
        if(usernames.contains(username)){
            for (int i=0; i < usernames.size(); i++){
                if(usernames.get(i).equals(username)){
                    timesUsernameExist = timesUsernameExist + 1;
                }
            }
        }else {
            return timesUsernameExist;
        }
        return  timesUsernameExist;

    }
    public void update(Trainee trainee) {
        traineeMap.put(trainee.getId(), trainee);
    }

    public void delete(Trainee trainee) {
        traineeMap.remove(trainee.getId());
    }

}
