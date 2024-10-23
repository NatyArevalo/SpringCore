package org.gym.Entities;

import com.sun.istack.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
public class Trainee extends User {

    @Past(message = "Date of birth must be in the past")
    @NotNull
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String address;

    public Trainee(Long userId, String firstName, String lastName, String username, String password, Boolean isActive, LocalDate dateOfBirth, String address) {
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Trainee{" + "id=" + super.getId() +
                ", firstName='" + super.getFirstName() + '\'' +
                ", lastName='" + super.getLastName() + '\'' +
                ", username='" + super.getUsername() + '\'' +
                ", password='" + super.getPassword() + '\'' +
                ", isActive=" + super.getIsActive() + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                '}';
    }
}
