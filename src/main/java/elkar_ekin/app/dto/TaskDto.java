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
    private Location locationId;
    private Category categoryId;
    private User volunteerId;
    
    public TaskDto() {
    }

    public TaskDto(String description, float estimatedTime, Date date, String state, Date startTime, Date endTime,
            Location locationId, Category categoryId, User volunteerId) {
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.date = date;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationId = locationId;
        this.categoryId = categoryId;
        this.volunteerId = volunteerId;
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

    public Location getLocationId() {
        return locationId;
    }

    public void setLocationId(Location locationId) {
        this.locationId = locationId;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public User getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(User volunteerId) {
        this.volunteerId = volunteerId;
    }

    
}
