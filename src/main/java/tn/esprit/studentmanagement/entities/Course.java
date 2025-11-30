package tn.esprit.studentmanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCourse;
    private String name;
    private String code;           // exemple : CS101
    private int credit;            // nombre de crédits
    private String description;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    Set<Enrollment> enrollments=new HashSet<>();

}
