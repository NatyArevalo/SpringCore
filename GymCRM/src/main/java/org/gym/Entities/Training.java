package org.gym.Entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.gym.Enumerators.TrainingType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter @Setter
public class Training {
    @Id
    private String id;

    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    @Enumerated(EnumType.STRING)
    private TrainingType specialization;
    @NotNull
    private LocalDate trainingDate;

    private double duration;

    @Override
    public String toString() {
        return "Training{" +
                "id='" + id + '\'' +
                ", traineeId=" + traineeId +
                ", trainerId=" + trainerId +
                ", trainingName='" + trainingName + '\'' +
                ", specialization=" + specialization +
                ", trainingDate=" + trainingDate +
                ", duration=" + duration +
                '}';
    }
}
