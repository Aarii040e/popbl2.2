package elkar_ekin.app.dto;

import java.sql.Date;

import elkar_ekin.app.model.Location;

public class UserDto {
	
	//Same fields as User except the ID

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String role;
    private String name;
    private String surname1;
    private String surname2;
    private String gender;
    private Date birthDate;
    private Location location;
    // private Long postCode;
    // private String direction;
    // private String town;
    // private String province;
    private String telephone;
    private String email;
    private String description;
    private String imagePath;

	public UserDto(){
	}

	public UserDto(String firstName, String lastName, String username, String password, String role, String name,
			String surname1, String surname2, String gender, Date birthDate, Location location, String telephone,
			String email, String description, String imagePath) {
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	

}