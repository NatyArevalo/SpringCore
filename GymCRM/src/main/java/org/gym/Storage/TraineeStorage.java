package org.gym.Storage;

import jakarta.annotation.PostConstruct;
import org.gym.Configuration.PasswordGenerator;
import org.gym.Entities.Trainee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class TraineeStorage {
    private static final Logger logger = Logger.getLogger(TrainerStorage.class.getName());
    private final ResourceLoader resourceLoader;

    private Map<Long, Trainee> traineeMap = new HashMap();
    private List<String> usernames = new ArrayList<>();
    @Value("${trainee.data.file}")
    private String dataFile;

    public TraineeStorage(ResourceLoader resourceLoader) {
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
        Integer timesUsernameExist = 0;
        Resource resource = resourceLoader.getResource("classpath:" + dataFile);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length < 8) {
                    logger.warning("Invalid data format: " + line);
                    continue;
                }
                Trainee trainee = new Trainee();
                trainee.setId(Long.valueOf(credentials[0]));
                trainee.setFirstName(credentials[1]);
                trainee.setLastName(credentials[2]);
                if(usernames.contains(credentials[1] + "." + credentials[2])){
                    for (String username : usernames) {
                        if (username.equals(credentials[1] + "." + credentials[2])) {
                            timesUsernameExist = timesUsernameExist + 1;
                        }
                    }
                    trainee.setUsername(credentials[1] + "." + credentials[2] + timesUsernameExist);
                } else{
                    trainee.setUsername(credentials[1] + "." + credentials[2]);
                }
                String generatedPassword = PasswordGenerator.generateRandomPassword();
                trainee.setPassword(generatedPassword);
                trainee.setIsActive(Boolean.valueOf(credentials[3]));
                trainee.setDateOfBirth(LocalDate.of(Integer.valueOf(credentials[4]), Integer.valueOf(credentials[5]), Integer.valueOf(credentials[6])));
                trainee.setAddress(credentials[7]);

                traineeMap.put(trainee.getId(), trainee);
            }
        }
    }
    public Map<Long, Trainee> getTraineeMap() {
        return traineeMap;
    }
}
