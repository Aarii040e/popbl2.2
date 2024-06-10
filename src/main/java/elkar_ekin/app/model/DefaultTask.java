package elkar_ekin.app.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "defaultTasks")
public class DefaultTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "default_taskid", nullable = false, updatable = false)
    private Long defaultTaskID;


    private String name;

    private Integer estimatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryID", nullable = false)
    private Category category;


    public DefaultTask() {
    }

    public DefaultTask(String name, Integer estimatedTime, Category category) {
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.category = category;
    }

    public Long getDefaultTaskID() {
        return defaultTaskID;
    }

    public void setDefaultTaskID(Long defaultTaskID) {
        this.defaultTaskID = defaultTaskID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }


    
}
