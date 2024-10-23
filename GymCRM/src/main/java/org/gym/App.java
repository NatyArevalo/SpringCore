package org.gym;


import org.gym.Configuration.AppConfiguration;
import org.gym.Configuration.GymFacade;
import org.gym.Entities.Trainee;
import org.gym.Entities.Trainer;
import org.gym.Entities.Training;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main( String[] args ){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);

        GymFacade gymFacade = context.getBean(GymFacade.class);

        Trainer newTrainer = gymFacade.createTrainer(Long.valueOf("11045123456"),"John", "Doe",  "true", "FUNCTIONAL");
        System.out.println("Created Trainer: " + newTrainer.toString());

        Trainer newTrainer1 = gymFacade.updateTrainer(Long.valueOf("11045123456"),"", "FUNCTIONAL");
        System.out.println("Created Trainer: " + newTrainer1.toString());

        Trainer newTrainer2 = gymFacade.createTrainer(Long.valueOf("11045123458"),"John", "Doe",  "true", "FUNCTIONAL");
        System.out.println("Created Trainer: " + newTrainer2.toString());

        Trainee newTrainee = gymFacade.createTrainee(Long.valueOf("1047555555"), "Catalina", "Jimenez", "true", 1995, 10,24, "Cartagena, Colombia");
        System.out.println("Created Trainee: " + newTrainee.toString());

        Training newTraining = gymFacade.createTraining(Long.valueOf("1047555555"), Long.valueOf("11045123456"), "Leg day", "STRENGTH", 2024, 10, 18, 1.5 );
        System.out.println("Created Training: " + newTraining.toString());

    }
}
