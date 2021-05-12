package wooteco.subway.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.Line;
import wooteco.subway.line.LineException;
import wooteco.subway.line.LineService;
import wooteco.subway.station.Station;
import wooteco.subway.station.StationService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class SectionServiceTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    @Autowired
    private SectionService sectionService;

    private Station aStation;
    private Station bStation;
    private Station cStation;
    private Station dStation;
    private Station eStation;

    private Line testLine;

    private Distance initialDistance = new Distance(10);
    private Distance insertDistance = new Distance(2);

    @BeforeEach
    private void initLine() {
        aStation = stationService.save(new Station("A역"));
        bStation = stationService.save(new Station("B역"));
        cStation = stationService.save(new Station("C역"));
        dStation = stationService.save(new Station("D역"));
        eStation = stationService.save(new Station("E역"));

        final Line line = new Line("코기선", "black", bStation.getId(), dStation.getId(), initialDistance.value());
        testLine = lineService.create(line);
    }

    @DisplayName("노선 추가 시 상행, 하행 종점 추가")
    @Test
    public void saveTest() {
        validateStationOrder(bStation, dStation);
        validateFinalStation(bStation, dStation);

        validateStationDistance(bStation, dStation, initialDistance);
    }

    @DisplayName("상행 종점역 -> 중간역 구간 등록")
    @Test
    public void addMiddleSectionFromFront() {
        sectionService.addSection(testLine.getId(), bStation.getId(), cStation.getId(), insertDistance.value());

        validateStationOrder(bStation, cStation, dStation);
        validateFinalStation(bStation, dStation);

        validateStationDistance(bStation, cStation, insertDistance);
        validateStationDistance(cStation, dStation, initialDistance.sub(insertDistance));
    }

    @DisplayName("중간역 -> 하행 종착역 구간 등록")
    @Test
    public void addMiddleSectionFromBack() {
        sectionService.addSection(testLine.getId(), cStation.getId(), dStation.getId(), insertDistance.value());

        validateStationOrder(bStation, cStation, dStation);
        validateFinalStation(bStation, dStation);

        validateStationDistance(bStation, cStation, initialDistance.sub(insertDistance));
        validateStationDistance(cStation, dStation, insertDistance);
    }

    @DisplayName("새로운 상행 종점역 구간 등록")
    @Test
    public void addFinalSectionFromFront() {
        sectionService.addSection(testLine.getId(), aStation.getId(), bStation.getId(), insertDistance.value());

        validateStationOrder(aStation, bStation, dStation);
        validateFinalStation(aStation, dStation);

        validateStationDistance(aStation, bStation, insertDistance);
        validateStationDistance(bStation, dStation, initialDistance);
    }

    @DisplayName("새로운 하행 종점역 구간 등록")
    @Test
    public void addFinalSectionFromDown() {
        sectionService.addSection(testLine.getId(), dStation.getId(), eStation.getId(), insertDistance.value());

        validateStationOrder(bStation, dStation, eStation);
        validateFinalStation(bStation, eStation);

        validateStationDistance(bStation, dStation, initialDistance);
        validateStationDistance(dStation, eStation, insertDistance);
    }

    @DisplayName("노선의 중간 역을 삭제한다.")
    @Test
    public void deleteStationInLine(){
        sectionService.addSection(testLine.getId(), cStation.getId(), dStation.getId(), insertDistance.value());
        sectionService.deleteSection(testLine.getId(), cStation.getId());

        validateStationOrder(bStation, dStation);
        validateFinalStation(bStation, dStation);

        validateStationDistance(bStation, dStation, initialDistance);
    }

    @DisplayName("노선의 상행 종점 역을 삭제한다.")
    @Test
    public void deleteFrontStationInLine(){
        sectionService.addSection(testLine.getId(), cStation.getId(), dStation.getId(), insertDistance.value());
        sectionService.deleteSection(testLine.getId(), bStation.getId());

        validateStationOrder(cStation, dStation);
        validateFinalStation(cStation, dStation);

        validateStationDistance(cStation, dStation, insertDistance);
    }

    @DisplayName("노선의 하행 종점 역을 삭제한다.")
    @Test
    public void deleteBackStationInLine(){
        sectionService.addSection(testLine.getId(), cStation.getId(), dStation.getId(), insertDistance.value());
        sectionService.deleteSection(testLine.getId(), dStation.getId());

        validateStationOrder(bStation, cStation);
        validateFinalStation(bStation, cStation);

        validateStationDistance(bStation, cStation, initialDistance.sub(insertDistance));
    }

    @DisplayName("노선이 종점 뿐 일 경우 역을 삭제할 수 없다.")
    @Test
    public void deleteFinalStation() {
        assertThatThrownBy(()->{
            sectionService.deleteSection(testLine.getId(), dStation.getId());
        }).isInstanceOf(LineException.class);
    }

    @DisplayName("노선에 존재하지 않는 역을 삭제할 수 없다.")
    @Test
    public void deleteNonExistentStation() {
        assertThatThrownBy(()->{
            sectionService.deleteSection(testLine.getId(), aStation.getId());
        }).isInstanceOf(LineException.class);
    }

    @DisplayName("노선에 둘 다 존재하지 않는 역으로 구간을 추가할 수 없다.")
    @Test
    public void addSectionWithNonExistentStations() {
        assertThatThrownBy(()->{
            sectionService.addSection(testLine.getId(), aStation.getId(), eStation.getId(), initialDistance.value());
        }).isInstanceOf(LineException.class);
    }

    @DisplayName("이미 존재하는 구간으로 구간을 추가할 수 없다.")
    @Test
    public void addSectionWithExistentSection() {
        assertThatThrownBy(()->{
            sectionService.addSection(testLine.getId(), aStation.getId(), bStation.getId(), initialDistance.value());
            validateStationOrder(aStation, bStation, dStation);
            sectionService.addSection(testLine.getId(), aStation.getId(), bStation.getId(), initialDistance.value());
        }).isInstanceOf(LineException.class);
    }

    @DisplayName("상행 -> 중간역 삽입 시 상행 - 하행의 거리보다 같거나 큰 거리로 추가할 수 없다.")
    @Test
    public void addFrontSectionWithInvalidDistance() {
        assertThatThrownBy(()->{
            final Distance biggerThanInitial = initialDistance.add(1);
            sectionService.addSection(testLine.getId(), bStation.getId(), cStation.getId(), biggerThanInitial.value());
        }).isInstanceOf(LineException.class);
    }

    @DisplayName("중간역 -> 하행 삽입 시 상행 - 하행의 거리보다 같거나 큰 거리로 추가할 수 없다.")
    @Test
    public void addBackSectionWithInvalidDistance() {
        assertThatThrownBy(()->{
            final Distance biggerThanInitial = initialDistance.add(1);
            sectionService.addSection(testLine.getId(), cStation.getId(), dStation.getId(), biggerThanInitial.value());
        }).isInstanceOf(LineException.class);
    }

    private void validateStationOrder(final Station... expectOrders) {
        final List<Long> stations = lineService.allStationIdInLine(testLine.getId());

        int index = 0;
        for (final Station station : expectOrders) {
            assertThat(stations.get(index++)).isEqualTo(station.getId());
        }
    }

    private void validateStationDistance(final Station upStation, final Station downStation, final Distance expectedDistance){
        final Distance actualDistance = sectionService.distance(testLine.getId(), upStation.getId(), downStation.getId());
        assertThat(expectedDistance).isEqualTo(actualDistance);
    }

    private void validateFinalStation(final Station expectedUpStation, final Station expectedDownStation){
        final List<Long> stations = lineService.allStationIdInLine(testLine.getId());
        final Long actualUpStationId = stations.get(0);
        final Long actualDownStationId = stations.get(stations.size()-1);

        assertThat(actualUpStationId).isEqualTo(expectedUpStation.getId());
        assertThat(actualDownStationId).isEqualTo(expectedDownStation.getId());
    }
}