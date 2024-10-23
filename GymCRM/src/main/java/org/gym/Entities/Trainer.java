package org.gym.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.gym.Enumerators.TrainingType;


import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Trainer extends User {
    @Enumerated(EnumType.STRING)
    private TrainingType specialization;

    public Trainer(Long userId, String firstName, String lastName, String username, String password, Boolean isActive, TrainingType specialization) {
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Trainer{" + "id=" + super.getId() +
                ", firstName='" + super.getFirstName() + '\'' +
                ", lastName='" + super.getLastName() + '\'' +
                ", username='" + super.getUsername() + '\'' +
                ", password='" + super.getPassword() + '\'' +
                ", isActive=" + super.getIsActive() + '\'' +
                ", specialization=" + specialization +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(getId(), trainer.getId()) && Objects.equals(getFirstName(), trainer.getFirstName()) && Objects.equals(getLastName(), trainer.getLastName()) && Objects.equals(getUsername(), trainer.getUsername()) && Objects.equals(getIsActive(), trainer.getIsActive()) && Objects.equals(getSpecialization(),trainer.getSpecialization());
    }

}
