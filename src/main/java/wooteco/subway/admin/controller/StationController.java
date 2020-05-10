package wooteco.subway.admin.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.StationService;

@RequestMapping("/stations")
@RestController
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping()
    public ResponseEntity createStation(@RequestBody StationCreateRequest stationCreateRequest) {
        Station station = stationCreateRequest.toStation();
        Station persistStation = stationService.addStation(station);

        return ResponseEntity
            .created(URI.create("/stations/" + persistStation.getId()))
            .body(StationResponse.of(persistStation));
    }

    @GetMapping("/{name}")
    public ResponseEntity findStation(@PathVariable String name) {
        Station station = stationService.findStationByName(name);
        return ResponseEntity.ok().body(station.getId());
    }

    @GetMapping()
    public ResponseEntity showStations() {
        return ResponseEntity.ok().body(stationService.showStations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStation(@PathVariable Long id) {
        stationService.removeStation(id);
        return ResponseEntity.noContent().build();
    }
}
