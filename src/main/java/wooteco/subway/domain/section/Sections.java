package wooteco.subway.domain.section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.station.Station;

public class Sections {

    private final List<Section> values;

    public Sections(List<Section> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("하나 이상의 구간이 존재해야 합니다.");
        }
        this.values = sortSectionFromUpToDown(values);
    }

    private List<Section> sortSectionFromUpToDown(List<Section> sections) {
        List<Section> sortedSections = new ArrayList<>();
        Section topSection = findTopSection(sections);

        sortedSections.add(topSection);
        for (int i = 0; i < sections.size() - 1; i++) {
            Section lastSection = sortedSections.get(sortedSections.size() - 1);
            for (Section section : sections) {
                if (lastSection.getDownStation().equals(section.getUpStation())) {
                    sortedSections.add(section);
                    break;
                }
            }
        }

        return sortedSections;
    }

    private Section findTopSection(List<Section> sections) {
        int numOfSections = sections.size();

        for (int i = 0; i < numOfSections; i++) {
            boolean isFound = true;
            Section topSection = sections.get(i);
            for (int j = 0; j < numOfSections; j++) {
                Section temp = sections.get(j);
                if (topSection.getUpStation().equals(temp.getDownStation())) {
                    isFound = false;
                    break;
                }
            }
            if (isFound) {
                return topSection;
            }
        }
        throw new IllegalArgumentException("상행 종점 구간을 찾을 수 없습니다.");
    }

    public boolean canAddToEndSection(Section section) {
        Section firstSection = values.get(0);
        Section lastSection = values.get(values.size() - 1);
        return section.getDownStation().equals(firstSection.getUpStation())
            || section.getUpStation().equals(lastSection.getDownStation());
    }

    public Section addToBetweenExistedSection(Section section) {
        validateBothStationExists(section.getUpStation(), section.getDownStation());
        Section existedSection = findBetweenExistedSection(section);
        return existedSection.splitedAndUpdate(section);
    }

    public void validateBothStationExists(Station upStation, Station downStation) {
        if (doesStationExists(upStation) && doesStationExists(downStation)) {
            throw new IllegalArgumentException("상행역과 하행역 모두 이미 해당 노선에 등록되어 있습니다.");
        }
        if (!doesStationExists(upStation) && !doesStationExists(downStation)) {
            throw new IllegalArgumentException("상행역과 하행역 모두 노선에 등록되어있지 않습니다.");
        }
    }

    private boolean doesStationExists(Station station) {
        return values.stream()
            .anyMatch(section -> section.exists(station));
    }

    private Section findBetweenExistedSection(Section section) {
        return values.stream()
            .filter(targetSection -> targetSection.getUpStation().equals(section.getUpStation()) || targetSection.getDownStation().equals(section.getDownStation()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 구간입니다."));
    }

    public boolean canRemoveEndSection(Station station) {
        validateMoreThanOneSection();
        Section firstSection = values.get(0);
        Section lastSection = values.get(values.size() - 1);
        return firstSection.getUpStation().equals(station) || lastSection.getDownStation().equals(station);
    }

    private void validateMoreThanOneSection() {
        if (values.size() == 1) {
            throw new IllegalArgumentException("해당 노선에 구간이 하나 남았으므로 구간을 제거할 수 없습니다.");
        }
    }

    public List<Section> findSectionsByStation(Station station) {
        List<Section> result =  values.stream()
            .filter(section -> section.exists(station))
            .collect(Collectors.toList());

        if (result.size() == 0) {
            throw new IllegalArgumentException("해당 지하철 역이 존재하는 구간은 없습니다.");
        }
        return result;
    }

    public List<Section> getValues() {
        return Collections.unmodifiableList(values);
    }
}