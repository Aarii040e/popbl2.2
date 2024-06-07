package elkar_ekin.app.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
	
	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Horrek primary keya automatikoki sortzen du
	private Long userID;

	private String username;
    private String password;
    private String role;
    private String name;
    private String surname1;
    private String surname2;
    private String gender;
    private Date birthDate;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locationID", nullable = false)
    private Location location;
    // private Long postCode;
    // private String direction;
    // private String town;
    // private String province;
    private String telephone;
    private String email;
    private String description;
    private String imagePath;

	@ManyToMany(mappedBy = "savedUsers")
    private Set<Task> savedTasks;
	
	@CreationTimestamp
    @Column(name = "createdAt", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;

	public User() {
		super();
	}


	// public User(String username, String password, String role, String name, String surname1, String surname2,
	// 		String gender, Date birthDate, Long postCode, String direction, String town, String province,
	// 		String telephone, String email, String description, String imagePath) {
	// 	this.username = username;
	// 	this.password = password;
	// 	this.role = role;
	// 	this.name = name;
	// 	this.surname1 = surname1;
	// 	this.surname2 = surname2;
	// 	this.gender = gender;
	// 	this.birthDate = birthDate;
	// 	this.postCode = postCode;
	// 	this.direction = direction;
	// 	this.town = town;
	// 	this.province = province;
	// 	this.telephone = telephone;
	// 	this.email = email;
	// 	this.description = description;
	// 	this.imagePath = imagePath;
	// }

	public User(String username, String password, String role, String name, String surname1, String surname2,
			String gender, Date birthDate, Location location, String telephone, String email, String description,
			String imagePath) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.name = name;
		this.surname1 = surname1;
		this.surname2 = surname2;
		this.gender = gender;
		this.birthDate = birthDate;
		this.location = location;
		this.telephone = telephone;
		this.email = email;
		this.description = description;
		this.imagePath = imagePath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname1() {
		return surname1;
	}

	public void setSurname1(String surname1) {
		this.surname1 = surname1;
	}

	public String getSurname2() {
		return surname2;
	}

	public void setSurname2(String surname2) {
		this.surname2 = surname2;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	
	// public String getDirection() {
	// 	return direction;
	// }

	// public void setDirection(String direction) {
	// 	this.direction = direction;
	// }

	// public String getTown() {
	// 	return town;
	// }

	// public void setTown(String town) {
	// 	this.town = town;
	// }

	// public String getProvince() {
	// 	return province;
	// }

	// public void setProvince(String province) {
	// 	this.province = province;
	// }
	
	// public Long getPostCode() {
	// 	return postCode;
	// }

	// public void setPostCode(Long postCode) {
	// 	this.postCode = postCode;
	// }

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Set<Task> getSavedTasks() {
		return savedTasks;
	}

	public void setSavedTasks(Set<Task> savedTasks) {
		this.savedTasks = savedTasks;
	}
	


}