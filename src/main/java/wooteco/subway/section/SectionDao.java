package wooteco.subway.section;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;
import wooteco.subway.domain.Section;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wooteco.subway.domain.Sections;

@Repository
public class SectionDao {

    private Long seq = 0L;
    private Map<Long, List<Section>> sections = new HashMap<>();

    private Section createNewObject(Section section) {
        Field field = ReflectionUtils.findField(Section.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, section, ++seq);
        return section;
    }

    public Section save(Section section, Long lineId) {
        List<Section> sectionsByLineId = sections.getOrDefault(lineId, new ArrayList<>());
        Section createdSection = createNewObject(section);
        sectionsByLineId.add(createdSection);
        sections.put(lineId, sectionsByLineId);
        return createdSection;
    }

    public Sections findSectionsByLineId(Long lineId) {
        final List<Section> sections = this.sections.get(lineId);
        return Sections.from(sections);
    }

    public Section saveAffectedSections(Section section, Optional<Section> affectedSection,
        Long lineId) {
        affectedSection.ifPresent(받아온것 -> {
            sections.get(lineId)
                .stream()
                .filter(안에있는것 -> 안에있는것.getId().equals(받아온것.getId()))
                .findAny()
                .ifPresent(안에있는것 -> {
                    sections.get(lineId).remove(안에있는것);
                    sections.get(lineId).add(받아온것);
            });
        });

        return save(section, lineId);
    }
}