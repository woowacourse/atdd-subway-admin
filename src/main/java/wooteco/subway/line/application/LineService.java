package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.domain.*;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.dto.SectionAddRequest;
import wooteco.subway.line.entity.LineEntity;
import wooteco.subway.line.entity.SectionEntity;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.domain.StationDao;
import wooteco.subway.station.dto.StationResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public LineResponse save(final LineRequest lineRequest) {
        validateDuplication(lineRequest);
        validateStations(lineRequest.getUpStationId(), lineRequest.getDownStationId());
        LineEntity savedLineEntity = lineDao.save(new LineEntity(lineRequest.getName(), lineRequest.getColor()));
        Station upStation = findStationById(lineRequest.getUpStationId());
        Station downStation = findStationById(lineRequest.getDownStationId());
        Line line = new Line(savedLineEntity.id(), savedLineEntity.name(), savedLineEntity.color());

        SectionEntity sectionEntity = sectionDao.save(new SectionEntity(line.getId(), upStation.getId(), downStation.getId(), lineRequest.getDistance()));
        Section section = new Section(sectionEntity.getId(), line, upStation, downStation, sectionEntity.getDistance());
        return new LineResponse(line.getId(), line.nameAsString(), line.getColor(), toStationsResponses(Collections.singletonList(section)));
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(final Long lineId) {
        LineEntity findLineEntity = findLineEntityById(lineId);
        Line line = new Line(findLineEntity.id(), findLineEntity.name(), findLineEntity.color());
        Sections sections = new Sections(toSections(line));

        List<Section> sortedSections = sections.sortedSections();
        return new LineResponse(line.getId(), line.nameAsString(), line.getColor(), toStationsResponses(sortedSections));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> new Line(lineEntity.id(), lineEntity.name(), lineEntity.color()))
                .map(line -> new LineResponse(line.getId(), line.nameAsString(), line.getColor(), toStationsResponses(new Sections(toSections(line)).sortedSections())))
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(final Long id, final LineUpdateRequest lineUpdateRequest) {
        LineEntity lineEntity = findLineEntityById(id);
        lineDao.update(lineEntity.id(), lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void delete(final Long id) {
        LineEntity lineEntity = findLineEntityById(id);
        lineDao.delete(lineEntity.id());
    }

    @Transactional
    public void addSection(final Long lineId, final SectionAddRequest sectionAddRequest) {
        LineEntity findLineEntity = findLineEntityById(lineId);
        Line line = new Line(findLineEntity.id(), findLineEntity.name(), findLineEntity.color());
        Sections originSections = new Sections(toSections(line));
        Section targetSection = new Section(findStationById(sectionAddRequest.getUpStationId()), findStationById(sectionAddRequest.getDownStationId()), sectionAddRequest.getDistance());

        line.addSection(targetSection);

        dirtyChecking(originSections, line.getSections());
    }

    @Transactional
    public void deleteSectionByStationId(final Long lineId, final Long stationId) {
        LineEntity findLineEntity = findLineEntityById(lineId);
        Line line = new Line(findLineEntity.id(), findLineEntity.name(), findLineEntity.color());
        Sections originSections = new Sections(toSections(line));
        Station targetStation = findStationById(stationId);

        line.deleteStation(targetStation);

        deleteDirtyChecking(originSections, line.getSections());
    }

    private void deleteDirtyChecking(final Sections originSections, final Sections sections) {
        List<Section> changedSections = sections.changedSections(originSections);
        for (Section section : changedSections) {
            SectionEntity changedSectionEntity = new SectionEntity(section.id(), section.line().getId(), section.upStation().getId(), section.downStation().getId(), section.distance());
            sectionDao.delete(changedSectionEntity.getId());

            Optional<Section> findStation = sections.findByUpwardStation(section.upStation());
            if (findStation.isPresent()) {
                Section saveSection = findStation.get();
                sectionDao.save(new SectionEntity(saveSection.line().getId(), saveSection.upStation().getId(), saveSection.downStation().getId(), saveSection.distance()));
            }
        }
    }

    private void dirtyChecking(final Sections originSections, final Sections sections) {
        List<Section> changedSections = originSections.changedSections(sections);
        for (Section section : changedSections) {
            SectionEntity changedSectionEntity = new SectionEntity(section.id(), section.line().getId(), section.upStation().getId(), section.downStation().getId(), section.distance());
            if (sectionDao.findByLineIdWithUpStationId(changedSectionEntity.getLineId(), changedSectionEntity.getUpStationId()).isPresent()) {
                sectionDao.deleteByLineIdWithUpStationId(changedSectionEntity.getLineId(), changedSectionEntity.getUpStationId());
            }

            if (sectionDao.findByLineIdWithDownStationId(changedSectionEntity.getLineId(), changedSectionEntity.getDownStationId()).isPresent()) {
                sectionDao.deleteByLineIdWithDownStationId(changedSectionEntity.getLineId(), changedSectionEntity.getDownStationId());
            }
            sectionDao.save(changedSectionEntity);
        }
    }

    private List<Section> toSections(final Line line) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(line.getId());
        return sectionEntities.stream()
                .map(sectionEntity -> new Section(sectionEntity.getId(), line, findStationById(sectionEntity.getUpStationId()), findStationById(sectionEntity.getDownStationId()), sectionEntity.getDistance()))
                .collect(Collectors.toList());
    }

    private List<StationResponse> toStationsResponses(final List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(
                        section.upStation(), section.downStation()
                ))
                .distinct()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    private void validateDuplication(final LineRequest lineRequest) {
        if (lineDao.findByName(lineRequest.getName()).isPresent()) {
            throw new IllegalStateException("이미 있는 역임!");
        }

        if (lineDao.findByColor(lineRequest.getColor()).isPresent()) {
            throw new IllegalStateException("이미 있는 색깔임!");
        }
    }

    private void validateStations(final Long upStationId, final Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new IllegalStateException("상행역과 하행역은 같을 수 없습니다");
        }
    }

    private LineEntity findLineEntityById(final Long id) {
        return lineDao.findById(id).orElseThrow(() -> new IllegalArgumentException("없는 노선임!"));
    }

    private Station findStationById(final Long stationId) {
        return stationDao.findById(stationId).orElseThrow(() -> new IllegalStateException("없는 역임!"));
    }
}
