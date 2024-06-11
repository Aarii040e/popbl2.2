package elkar_ekin.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import elkar_ekin.app.dto.LocationDto;
import elkar_ekin.app.model.Location;
import elkar_ekin.app.repositories.LocationRepository;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepository;

	//Save location data based on the provided LocationDto.
	@Override
	public Location saveLocation(LocationDto locationDto) {
		Location location = new Location(locationDto.getPostCode(), locationDto.getDirection(), locationDto.getTown(), locationDto.getProvince());
		return locationRepository.save(location);
	}

}
