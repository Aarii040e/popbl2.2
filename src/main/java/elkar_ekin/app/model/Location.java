package elkar_ekin.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "locations")
public class Location {
	
	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Horrek primary keya automatikoki sortzen du
	private Long locationID;

    private Long postCode;
    private String direction;
    private String town;
    private String province;

	public Location() {
		super();
	}

	
	public Location(Long postCode, String direction, String town, String province) {
		this.postCode = postCode;
		this.direction = direction;
		this.town = town;
		this.province = province;
	}


	public Long getLocationID() {
		return locationID;
	}


	public void setLocationID(Long locationID) {
		this.locationID = locationID;
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

}