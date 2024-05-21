package elkar_ekin.app.dto;

import java.sql.Date;

import elkar_ekin.app.model.Location;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {
	
	//Same fields as User except the ID

	@NotBlank(message = "{NotBlank.userDto.firstName}")
    private String firstName;

    @NotBlank(message = "{NotBlank.userDto.lastName}")
    private String lastName;

    @NotBlank(message = "{NotBlank.userDto.username}")
    private String username;

    @NotBlank(message = "{NotBlank.userDto.password}")
    @Size(min = 6, message = "{Size.userDto.password}")
    private String password;

    private String role;

    @NotBlank(message = "{NotBlank.userDto.name}")
    private String name;

    @NotBlank(message = "{NotBlank.userDto.surname1}")
    private String surname1;

    private String surname2;

    @NotBlank(message = "{NotBlank.userDto.gender}")
    private String gender;

    private Date birthDate;

    private Location location;

    // private Long postCode;

    // private String direction;

    // private String town;

    // private String province;

    @Pattern(regexp = "\\d{10}", message = "{Pattern.userDto.telephone}")
    private String telephone;

    @NotBlank(message = "{NotBlank.userDto.email}")
    @Email(message = "{Email.userDto.email}")
    private String email;

    private String description;

    private String imagePath;

	public UserDto(){
	}



	
	// public UserDto(String username, String password, String role, String name, String surname1, String surname2,
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


	public UserDto(@NotBlank(message = "{NotBlank.userDto.firstName}") String firstName,
			@NotBlank(message = "{NotBlank.userDto.lastName}") String lastName,
			@NotBlank(message = "{NotBlank.userDto.username}") String username,
			@NotBlank(message = "{NotBlank.userDto.password}") @Size(min = 6, message = "{Size.userDto.password}") String password,
			String role, @NotBlank(message = "{NotBlank.userDto.name}") String name,
			@NotBlank(message = "{NotBlank.userDto.surname1}") String surname1, String surname2,
			@NotBlank(message = "{NotBlank.userDto.gender}") String gender, Date birthDate, Location location,
			@Pattern(regexp = "\\d{10}", message = "{Pattern.userDto.telephone}") String telephone,
			@NotBlank(message = "{NotBlank.userDto.email}") @Email(message = "{Email.userDto.email}") String email,
			String description, String imagePath) {
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


	// public Long getPostCode() {
	// 	return postCode;
	// }


	// public void setPostCode(Long postCode) {
	// 	this.postCode = postCode;
	// }


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




	public Location getLocation() {
		return location;
	}




	public void setLocation(Location location) {
		this.location = location;
	}

	

}