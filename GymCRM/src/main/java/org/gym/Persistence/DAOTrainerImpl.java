package org.gym.Persistence;

import org.gym.DAO.TrainerDAO;
import org.gym.Entities.Trainer;
import org.gym.Storage.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DAOTrainerImpl implements TrainerDAO {
    private final Map<Long, Trainer> trainerMap;
    private final List<String> usernames;
    @Autowired
    public DAOTrainerImpl (TrainerStorage trainerStorage, List<String> usernames) {
        this.trainerMap = trainerStorage.getTrainerMap();
        this.usernames = usernames;
    }
    public List<Trainer> getAll() {
        return new ArrayList<Trainer>(trainerMap.values());
    }
    public Trainer getTrainerById(Long userId) {
        return trainerMap.get(userId);
    }

    public void create(Trainer trainer) {
        usernames.add(trainer.getFirstName() + "." + trainer.getLastName());
        trainerMap.put(trainer.getId(), trainer);

    }

    public Integer validateUsername(String username){
        Integer timesUsernameExist = 0;
        if(usernames.contains(username)){
            for (String s : usernames) {
                if (s.equals(username)) {
                    timesUsernameExist = timesUsernameExist + 1;
                }
            }
        }else {
            return timesUsernameExist;
        }
        return  timesUsernameExist;

    }

    public void update(Trainer trainer) {
        trainerMap.put(trainer.getId(), trainer);
    }

    public void delete(Trainer trainer) {
        trainerMap.remove(trainer.getId());
    }
}
