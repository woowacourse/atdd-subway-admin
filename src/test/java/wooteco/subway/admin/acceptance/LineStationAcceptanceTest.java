package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.service.LineStationService;

import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineStationAcceptanceTest {

    @Mock
    private LineRepository lineRepository;

    private Line line;
    private Station preStation;
    private Station station;
    private LineStation lineStation;

    private LineStationService lineStationService;

    @BeforeEach
    void setUp() {
        lineStationService = new LineStationService(lineRepository);
        line = new Line(1L,"1호선", "bg-red-500", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        preStation = new Station(2L, "사당역");
        station = new Station(3L, "강남역");
    }

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        // When 지하철 노선에 지하철 역을 등록하는 요청을 한다.
        // Then 지하철역이 노선에 추가 되었다.
        when(lineRepository.findById(1L)).thenReturn(Optional.of(line));

        lineStation = lineStationService.createLineStation(1L, 2L, 3L, 1, 1);
        assertThat(lineStation.getLine()).isEqualTo(1L);
        assertThat(lineStation.getPreStationId()).isEqualTo(2L);
        assertThat(lineStation.getStationId()).isEqualTo(3L);

        // When 지하철 노선의 지하철역 목록 조회 요청을 한다.
        // Then 지하철역 목록을 응답 받는다.
        // And 새로 추가한 지하철역을 목록에서 찾는다.
        when(lineRepository.findById(1L)).thenReturn(Optional.of(line));

        Set<LineStation> lineStations = lineStationService.findLineStation(line.getId());
        assertThat(lineStations.contains(lineStation)).isTrue();

        // When 지하철 노선에 포함된 특정 지하철역을 제외하는 요청을 한다.
        // Then 지하철역이 노선에서 제거 되었다.

        when(lineRepository.findById(1L)).thenReturn(Optional.of(line));

        LineStation removedLineStation = lineStationService.removeLineStation(line.getId(),
                station.getId());
        assertThat(lineStation).isEqualTo(removedLineStation);

        // When 지하철 노선의 지하철역 목록 조회 요청을 한다.
        // Then 지하철역 목록을 응답 받는다.
        // And 제외한 지하철역이 목록에 존재하지 않는다.
        lineStations = lineStationService.findLineStation(line.getId());

        assertThat(lineStations.contains(removedLineStation)).isFalse();
    }
}
