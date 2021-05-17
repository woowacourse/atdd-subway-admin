package wooteco.subway.station.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StationsTest {

    @DisplayName("지하철 역의 컬렉션(List)를 가진 Stations 객체 생성된다.")
    @Test
    void create() {
        //given
        List<Station> stationList = Arrays.asList(
                new Station(1L, "첫 번째 역 이름"),
                new Station(2L, "두 번째 역 이름")
        );

        //when
        Stations stations = new Stations(stationList);

        //then
        assertThat(stations).isInstanceOf(Stations.class);
    }

    @DisplayName("정렬된 역들의 Id를 활용해서 정렬한다.")
    @Test
    void sortStationsByIds() {
        //given
        Stations stations = new Stations(Arrays.asList(
                new Station(4L, "네 번째 역 이름"),
                new Station(1L, "첫 번째 역 이름"),
                new Station(2L, "두 번째 역 이름"),
                new Station(3L, "세 번째 역 이름")
        ));
        List<Long> sortIds = Arrays.asList(1L, 2L, 3L, 4L);

        //when
        Stations sortedStations = stations.sortStationsByIds(sortIds);

        //then
        assertThat(sortedStations.toList()).containsExactly(new Station(1L, "첫 번째 역 이름"),
                new Station(2L, "두 번째 역 이름"),
                new Station(3L, "세 번째 역 이름"),
                new Station(4L, "네 번째 역 이름"));
    }
}