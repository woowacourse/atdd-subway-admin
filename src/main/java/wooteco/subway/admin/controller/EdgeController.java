package wooteco.subway.admin.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.EdgeCreateRequest;
import wooteco.subway.admin.dto.EdgeDeleteRequest;
import wooteco.subway.admin.service.LineService;

@RestController
@RequestMapping("lines/{id}/edges")
public class EdgeController {
	private final LineService lineService;

	public EdgeController(final LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<Long> createEdge(@PathVariable(name = "id") final Long lineId,
		@RequestBody @Valid final EdgeCreateRequest edgeCreateRequest) {
		lineService.addEdge(lineId, edgeCreateRequest);
		return new ResponseEntity<>(1L, HttpStatus.CREATED);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteEdge(@PathVariable(name = "id") final Long lineId,
		@RequestBody @Valid final EdgeDeleteRequest edgeDeleteRequest) {
		lineService.removeEdge(lineId, edgeDeleteRequest.getStationId());
		return ResponseEntity.noContent().build();
	}
}
