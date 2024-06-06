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
@Table(name = "tasks")
public class Task {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Horrek primary keya automatikoki sortzen du
    private Long taskID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskDefaultID", nullable = false)
    private DefaultTask taskDefaultID;

	@Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate date;
    private String state;
    private LocalTime  startTime;
    private LocalTime  endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationID", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientID", nullable = false)
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteerID", nullable = true)
    private User volunteer;

    public Task() {
    }

    public Task(DefaultTask taskDefaultID, String description, LocalDate date, String state, LocalTime startTime,
            LocalTime endTime, Location location, User client, User volunteer) {
        this.taskDefaultID = taskDefaultID;
        this.description = description;
        this.date = date;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.client = client;
        this.volunteer = volunteer;
    }

    

    public Long getTaskID() {
        return taskID;
    }

    // public Task(DefaultTask taskDefaultID, String description, LocalDate date, String state, LocalTime startTime,
    //         LocalTime endTime, Location location, User client) {
    //     this.taskDefaultID = taskDefaultID;
    //     this.description = description;
    //     this.date = date;
    //     this.state = state;
    //     this.startTime = startTime;
    //     this.endTime = endTime;
    //     this.location = location;
    //     this.client = client;
    // }

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(User volunteer) {
        this.volunteer = volunteer;
    }

    public DefaultTask getTaskDefaultID() {
        return taskDefaultID;
    }

    public void setTaskDefaultID(DefaultTask taskDefaultID) {
        this.taskDefaultID = taskDefaultID;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
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