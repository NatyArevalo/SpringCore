package org.gym.Storage;

import jakarta.annotation.PostConstruct;
import org.gym.Configuration.PasswordGenerator;
import org.gym.Entities.Trainer;
import org.gym.Enumerators.TrainingType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TrainerStorage {
    private static final Logger logger = Logger.getLogger(TrainerStorage.class.getName());
    private final ResourceLoader resourceLoader;
    private Map<Long, Trainer> trainerMap = new HashMap();
    private List<String> usernames = new ArrayList<>();
    @Value("${trainer.data.file}")
    private String dataFile;

    public TrainerStorage(ResourceLoader resourceLoader) {
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
                if (credentials.length < 5) {
                    logger.warning("Invalid data format: " + line);
                    continue;
                }
                Trainer trainer = new Trainer();
                trainer.setId(Long.valueOf(credentials[0]));
                trainer.setFirstName(credentials[1]);
                trainer.setLastName(credentials[2]);

                if(usernames.contains(credentials[1] + "." + credentials[2])){
                    for (String username : usernames) {
                        if (username.equals(credentials[1] + "." + credentials[2])) {
                            timesUsernameExist = timesUsernameExist + 1;
                        }
                    }
                    trainer.setUsername(credentials[1] + "." + credentials[2] + timesUsernameExist);
                } else{
                    trainer.setUsername(credentials[1] + "." + credentials[2]);
                }

                String generatedPassword = PasswordGenerator.generateRandomPassword();
                trainer.setPassword(generatedPassword);
                trainer.setIsActive(Boolean.valueOf(credentials[3]));
                trainer.setSpecialization(TrainingType.valueOf(credentials[4]));

                trainerMap.put(trainer.getId(), trainer);
                usernames.add(trainer.getUsername());
            }
        }
    }
    public Map<Long, Trainer> getTrainerMap() {
        return trainerMap;
    }
}
