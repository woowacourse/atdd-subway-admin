package wooteco.subway.line.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.line.Line;
import wooteco.subway.line.LineDao;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @DisplayName("구간을 생성한다.")
    @Test
    void save() {
        final Line createdLine = lineDao.save(new Line("2호선", "black"));

        final Section section = new Section(createdLine.getId(), 2L, 4L, 10);
        final Section createdSection = sectionDao.save(section);

        assertThat(createdSection)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(section);
    }

    @DisplayName("동일한 노선의 전체 구간을 조회한다.")
    @Test
    void findByLineId() {
        final Line createdLine = lineDao.save(new Line("2호선", "black"));
        final List<Section> sections = new ArrayList<>(Arrays.asList(
            new Section(createdLine.getId(), 2L, 4L, 10),
            new Section(createdLine.getId(), 4L, 6L, 20)
        ));
        sections.forEach(section -> sectionDao.save(section));

        assertThat(sectionDao.findByLineId(createdLine.getId()))
            .usingElementComparatorIgnoringFields("id")
            .isEqualTo(sections);
    }

    @DisplayName("특정 이름의 구간을 조회한다.")
    @Test
    void findByName() {
        final Line createdLine = lineDao.save(new Line("2호선", "black"));
        final Section section = new Section(createdLine.getId(), 2L, 4L, 10);
        final Section createdSection = sectionDao.save(section);

        assertThat(sectionDao.findById(createdSection.getId()).get())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(section);
    }

    @DisplayName("특정 구간을 수정한다.")
    @Test
    void update() {
        final Line createdLine = lineDao.save(new Line("2호선", "black"));
        final Section section = new Section(createdLine.getId(), 2L, 4L, 10);
        final Section createdSection = sectionDao.save(section);

        final Section updatedSection = new Section(
            createdSection.getId(), createdLine.getId(), 4L, 6L, 20
        );
        sectionDao.update(updatedSection);

        assertThat(sectionDao.findById(createdSection.getId()).get()).isEqualTo(updatedSection);
    }
}