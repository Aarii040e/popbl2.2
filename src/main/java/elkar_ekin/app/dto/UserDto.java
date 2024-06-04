package elkar_ekin.app.dto;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import elkar_ekin.app.model.Location;

public class UserDto {
	
	Long userID;
    private String name;
    private String surname1;
    private String surname2;
    private String username;
    private String password;
    private String role;
    private String gender;
    private Date birthDate;
    private Location location;
    private String telephone;
    private String email;
    private String description;
    private MultipartFile imageFile;

	public UserDto(){
	}

	public UserDto(Long userID, String name, String surname1, String surname2, String username, String password,
			String role, String gender, Date birthDate, Location location, String telephone, String email,
			String description) {
		this.userID = userID;
		this.name = name;
		this.surname1 = surname1;
		this.surname2 = surname2;
		this.username = username;
		this.password = password;
		this.role = role;
		this.gender = gender;
		this.birthDate = birthDate;
		this.location = location;
		this.telephone = telephone;
		this.email = email;
		this.description = description;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

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

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	// public String getImagePath() {
	// 	return imagePath;
	// }

	// public void setImagePath(String imagePath) {
	// 	this.imagePath = imagePath;
	// }

	

}