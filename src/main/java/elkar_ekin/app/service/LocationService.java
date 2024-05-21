package elkar_ekin.app.service;


import elkar_ekin.app.dto.LocationDto;
import elkar_ekin.app.model.Location;


public interface LocationService {
	Location saveLocation (LocationDto locationDto);

}
