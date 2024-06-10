package elkar_ekin.app.dto;

public class LocationDto {
	
    //Parameters
    private String postCode;
    private String direction;
    private String town;
    private String province;

    
    //Constructors
	public LocationDto(){
	}

	public LocationDto(String postCode, String direction, String town, String province) {
		this.postCode = postCode;
		this.direction = direction;
		this.town = town;
		this.province = province;
	}
	
    //Getters and Setters
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
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


	

	}