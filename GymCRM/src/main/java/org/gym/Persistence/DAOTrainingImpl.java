package org.gym.Persistence;

import org.gym.DAO.TrainingDAO;
import org.gym.Entities.Training;
import org.gym.Storage.TrainingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Repository
public class DAOTrainingImpl implements TrainingDAO {
    private final Map<String, Training> trainingMap;
    @Autowired
    public DAOTrainingImpl (TrainingStorage trainingStorage) {
        this.trainingMap = trainingStorage.getTrainingMap();
    }
    public List<Training> getAll() {
        return new ArrayList<Training>(trainingMap.values());
    }

    public Training getTrainingById(String userId) {
        return trainingMap.get(userId);
    }


    public void create(Training training) {
        trainingMap.put(training.getId(), training);
    }

    public void update(Training training) {
        trainingMap.put(training.getId(), training);
    }

    public void delete(Training training) {
        trainingMap.remove(training.getId());
    }
}
