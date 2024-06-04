package elkar_ekin.app.model;

import java.sql.Date;
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
public class HistoricTasks {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Horrek primary keya automatikoki sortzen du
    private Long taskID;
    private Long defaultTaskID;
	@Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate date;
    private String state;
    private LocalTime  startTime;
    private LocalTime  endTime;
    private Long locationID;
    private Long clientId;
    private Long volunteerId;

    public HistoricTasks() {
    }    

    public HistoricTasks(Long taskID, Long defaultTaskID, String description, LocalDate date, String state,
            LocalTime startTime, LocalTime endTime, Long locationID, Long clientId, Long volunteerId) {
        this.taskID = taskID;
        this.defaultTaskID = defaultTaskID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getLocation() {
        return locationID;
    }

    public void setLocation(Location location) {
        this.locationID = location.getLocationID();
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteer(User volunteer) {
        this.volunteerId = volunteer.getUserID();
    }

    public Long getTaskDefaultID() {
        return defaultTaskID;
    }

    public void setTaskDefaultID(DefaultTask defaultTaskID) {
        this.defaultTaskID = defaultTaskID.getDefaultTaskID();
    }

    public Long getClient() {
        return clientId;
    }

    public void setClient(User client) {
        this.clientId = client.getUserID();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
    

}