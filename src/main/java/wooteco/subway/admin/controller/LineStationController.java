package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.LineStationsResponse;
import wooteco.subway.admin.service.LineService;

@RequestMapping(("/lines"))
@RestController
public class LineStationController {
    private final LineService lineService;

    public LineStationController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/{lineId}/stations")
    ResponseEntity<LineResponse> addLineStation(@PathVariable Long lineId,
            @RequestBody LineStationCreateRequest lineStationCreateRequest) {
        Line line = lineService.addLineStation(lineId, lineStationCreateRequest);

        return ResponseEntity
                .created(URI.create("/lines/" + lineId + "/stations/" + lineStationCreateRequest.getStationId()))
                .body(LineResponse.of(line));
    }

    @GetMapping("/stations")
    ResponseEntity<List<LineStationsResponse>> getLinesStations() {
        List<LineStationsResponse> linesStations = lineService.findAllLineStations();

        return ResponseEntity
                .ok(linesStations);
    }

    @DeleteMapping("{lineId}/stations/{stationId}")
    ResponseEntity<Void> deleteLineStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.removeLineStation(lineId, stationId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
