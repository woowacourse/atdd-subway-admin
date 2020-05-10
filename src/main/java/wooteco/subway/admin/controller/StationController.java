package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.StationRepository;

@RestController
public class StationController {
	private final StationRepository stationRepository;

	public StationController(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@GetMapping("/stations")
	public ResponseEntity showStations(
		@RequestParam(value = "name", required = false) String name) {
		if (Objects.isNull(name)) {
			return ResponseEntity.ok().body(StationResponse.of(stationRepository.findAll()));
		}
		if (name.isEmpty()) {
			return ResponseEntity.ok().body(null);
		}
		Station station = stationRepository.findByName(name)
		                                   .orElseThrow(NoSuchElementException::new);
		return ResponseEntity.ok().body(StationResponse.of(station));
	}

	@PostMapping("/stations")
	public ResponseEntity createStation(@RequestBody StationCreateRequest view) {
		Station station = view.toStation();
		Station persistStation = stationRepository.save(station);

		return ResponseEntity
			.created(URI.create("/stations/" + persistStation.getId()))
			.body(StationResponse.of(persistStation));
	}

	@DeleteMapping("/stations/{id}")
	public ResponseEntity deleteStation(@PathVariable Long id) {
		stationRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
