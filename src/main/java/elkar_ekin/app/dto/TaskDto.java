package elkar_ekin.app.dto;

import java.sql.Date;

import elkar_ekin.app.model.Category;
import elkar_ekin.app.model.Location;
import elkar_ekin.app.model.User;

public class TaskDto {
    
    private String description;
    private float estimatedTime;
    private Date date;
    private String state;
    private Date startTime;
    private Date endTime;
    private Location location;
    private Category category;
    private User volunteer;
    
    public TaskDto() {
    }

    public TaskDto(String description, float estimatedTime, Date date, String state, Date startTime, Date endTime,
            Location location, Category category, User volunteer) {
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.date = date;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.category = category;
        this.volunteer = volunteer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(float estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(User volunteer) {
        this.volunteer = volunteer;
    }

    
}
