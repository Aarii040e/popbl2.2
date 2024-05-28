package elkar_ekin.app.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import elkar_ekin.app.model.DefaultTask;
import elkar_ekin.app.model.Location;
import elkar_ekin.app.model.User;

public class TaskDto {
    
    private DefaultTask taskDefaultID;
    private String description;
    private LocalDate date;
    private String state;
    private LocalTime startTime;
    private LocalTime endTime;
    private Location location;
    private User volunteer;
    private User client;
    
    public TaskDto() {
    }


    public TaskDto(DefaultTask taskDefaultID, String description, LocalDate date, String state, LocalTime startTime,
            LocalTime endTime, Location location, User volunteer, User client) {
        this.taskDefaultID = taskDefaultID;
        this.description = description;
        this.date = date;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.volunteer = volunteer;
        this.client = client;
    }

    public DefaultTask getTaskDefaultID() {
        return taskDefaultID;
    }

    public void setTaskDefaultID(DefaultTask taskDefaultID) {
        this.taskDefaultID = taskDefaultID;
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
