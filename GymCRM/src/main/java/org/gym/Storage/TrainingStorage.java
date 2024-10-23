package org.gym.Storage;

import jakarta.annotation.PostConstruct;
import org.gym.Entities.Training;
import org.gym.Enumerators.TrainingType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TrainingStorage {
    private static final Logger logger = Logger.getLogger(TrainerStorage.class.getName());

    private final ResourceLoader resourceLoader;
    private Map<String, Training> trainingMap = new HashMap();
    @Value("${training.data.file}")
    private String dataFile;
    public TrainingStorage(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @PostConstruct
    public void init() {
        try {
            loadDataFromFile();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading trainer data from file: " + dataFile, e);
            throw new RuntimeException("Failed to load trainer data", e);
        }
    }

    private void loadDataFromFile() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + dataFile);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length < 8) {
                    logger.warning("Invalid data format: " + line);
                    continue;
                }
                Training training = new Training();
                training.setId(UUID.randomUUID().toString());
                training.setTrainerId(Long.valueOf(credentials[0]));
                training.setTraineeId(Long.valueOf(credentials[1]));
                training.setTrainingName(credentials[2]);
                training.setSpecialization(TrainingType.valueOf(credentials[3]));
                training.setTrainingDate(LocalDate.of(Integer.valueOf(credentials[4]), Integer.valueOf(credentials[5]), Integer.valueOf(credentials[6])));
                training.setDuration(Double.valueOf(credentials[7]));

                trainingMap.put(training.getId(), training);
            }
        }
    }
    public Map<String, Training> getTrainingMap() {
        return trainingMap;
    }
}
