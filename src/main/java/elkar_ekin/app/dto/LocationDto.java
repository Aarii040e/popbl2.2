package elkar_ekin.app.dto;

public class LocationDto {
	
	//Same fields as User except the ID

    private Long postCode;

    private String direction;

    private String town;

    private String province;

    

	public LocationDto(){
	}

	public LocationDto(Long postCode, String direction, String town, String province) {
		this.postCode = postCode;
		this.direction = direction;
		this.town = town;
		this.province = province;
	}

	public Long getPostCode() {
		return postCode;
	}

	public void setPostCode(Long postCode) {
		this.postCode = postCode;
	}

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