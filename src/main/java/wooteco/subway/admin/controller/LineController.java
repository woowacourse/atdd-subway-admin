package wooteco.subway.admin.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.service.LineService;

@RequestMapping("/lines")
@RestController
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody LineRequest request) {
        Line saved = lineService.save(request.toLine());
        return ResponseEntity.created(URI.create("/lines/" + saved.getId())).build();
    }

    @GetMapping
    public ResponseEntity showLines() {
        return ResponseEntity.ok(lineService.findAllLinesWithStationsById());
    }

    @GetMapping("/{id}")
    public ResponseEntity showLine(@PathVariable("id") Long id) {
        return ResponseEntity.ok(lineService.findLineWithStationsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLine(@PathVariable("id") Long id,
        @RequestBody LineRequest request) {
        lineService.updateLine(id, request.toLine());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable("id") Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity addStation(
        @PathVariable("id") Long id,
        @RequestBody LineStationCreateRequest request
    ) {
        lineService.addLineStation(id, request);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity deleteStationFromLine(
        @PathVariable("lineId") Long lineId,
        @PathVariable("stationId") Long stationId
    ) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
