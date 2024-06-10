package elkar_ekin.app.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "historic_tasks")
public class HistoricTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false, updatable = false)
    private Long taskID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_taskid", nullable = false)
    private DefaultTask taskDefault;


    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate date;
    private String state;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long locationID;
    private Long clientId;
    private Long volunteerId;

    public HistoricTask() {
    }

    public HistoricTask(Long taskID, DefaultTask taskDefault, String description, LocalDate date, String state,
            LocalTime startTime, LocalTime endTime, Long locationID, Long clientId, Long volunteerId) {
        this.taskID = taskID;
        this.taskDefault = taskDefault;
        this.description = description;
        this.date = date;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationID = locationID;
        this.clientId = clientId;
        this.volunteerId = volunteerId;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public DefaultTask getTaskDefaultID() {
        return taskDefault;
    }

    public void setTaskDefaultID(DefaultTask taskDefaultID) {
        this.taskDefault = taskDefaultID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Long getLocationID() {
        return locationID;
    }

    public void setLocationID(Long locationID) {
        this.locationID = locationID;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
    }
}
