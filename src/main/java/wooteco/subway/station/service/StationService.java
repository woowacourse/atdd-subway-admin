package wooteco.subway.station.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.station.StationDuplicatedNameException;
import wooteco.subway.section.dto.response.SectionResponse;
import wooteco.subway.station.Station;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StationService {
    private static final Logger log = LoggerFactory.getLogger(StationService.class);

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse save(StationRequest stationRequest) {
        validatesNameDuplication(stationRequest);
        Station station = new Station(stationRequest.getName());
        Station newStation = stationDao.save(station);
        log.info("{} 이 생성되었습니다.", newStation.getName());
        return new StationResponse(newStation.getId(), newStation.getName());
    }

    private void validatesNameDuplication(StationRequest stationRequest) {
        stationDao.findByName(stationRequest.getName())
                .ifPresent(l -> {
                    throw new StationDuplicatedNameException();
                });
    }

    public List<StationResponse> findAll() {
        List<Station> stations = stationDao.findAll();
        log.info("등록된 지하철 역 조회 성공");
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        stationDao.delete(id);
        log.info("지하철 역 삭제 성공");
    }

    public void checkRightStation(Long upStationId, Long downStationId) {
        validatesSameStation(upStationId, downStationId);
        validatesExistStation(upStationId);
        validatesExistStation(downStationId);
    }

    private void validatesSameStation(Long upStationId, Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new SubwayException("같은 역을 등록할 수 없습니다.");
        }
    }

    private void validatesExistStation(Long id) {
        if (Optional.empty().equals(stationDao.findById(id))) {
            throw new SubwayException("존재하지 않는 역입니다.");
        }
    }

    public List<StationResponse> findStations(List<SectionResponse> sections) {
        Set<Long> upStationIds = sections.stream()
                .map(SectionResponse::getUpStationId).collect(Collectors.toSet());
        Set<Long> downStationIds = sections.stream()
                .map(SectionResponse::getDownStationId).collect(Collectors.toSet());
        Set<Long> allStationIds = new HashSet<>(upStationIds);
        allStationIds.addAll(downStationIds);
        return allStationIds.stream()
                .map(stationDao::findById)
                .map(Optional::get)
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
