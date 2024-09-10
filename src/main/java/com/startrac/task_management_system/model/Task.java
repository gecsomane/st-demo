package com.startrac.task_management_system.model;


import com.startrac.task_management_system.enumerated.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Task {

    @Id
    @SequenceGenerator(name = "task_sequence", sequenceName = "task_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_sequence")
    private Short id;

    private String title;

    private String descripton;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserData userData;


    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescripton() {
        return descripton;
    }

    public void setDescripton(String descripton) {
        this.descripton = descripton;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UserData getUser() {
        return userData;
    }

    public void setUser(UserData userData) {
        this.userData = userData;
    }
}
