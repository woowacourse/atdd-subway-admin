package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.StationService;

@RestController
public class StationController {
	private final StationService stationService;

	public StationController(StationService stationService) {
		this.stationService = stationService;
	}

	@GetMapping("/stations")
	public ResponseEntity<List<StationResponse>> showStations() {
		return ResponseEntity.ok().body(StationResponse.of(stationService.findAllStations()));
	}

	@PostMapping("/stations")
	public ResponseEntity<Void> createStation(@RequestBody StationCreateRequest view) {
		Station station = view.toStation();
		Station persistStation = stationService.save(station);

		return ResponseEntity
			.created(URI.create("/stations/" + persistStation.getId())).build();
	}

	@DeleteMapping("/stations/{id}")
	public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
		stationService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
