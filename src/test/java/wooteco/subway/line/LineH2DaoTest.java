package wooteco.subway.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LineH2DaoTest {

    @Autowired
    private LineDao lineDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE LINE");
        jdbcTemplate.execute("ALTER TABLE LINE ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("INSERT INTO LINE (name, color) VALUES (?, ?)", "2호선", "초록색");
    }

    @DisplayName("노선 저장 테스트")
    @Test
    void save() {
        Line line = new Line("3호선", "주황색");
        Line savedLine = lineDao.save(line);

        assertThat(savedLine.getName()).isEqualTo(line.getName());
        assertThat(savedLine.getColor()).isEqualTo(line.getColor());
    }

    @DisplayName("노선 목록 조회 테스트")
    @Test
    void findAll() {
        Line line = new Line("3호선", "주황색");
        lineDao.save(line);

        assertThat(lineDao.findAll()).hasSize(2);
    }

    @DisplayName("노선 수정 테스트")
    @Test
    void update() {
        Long id = 1L;
        String name = "3호선";
        String color = "주황색";

        lineDao.update(id, name, color);
        Line line = lineDao.findById(id).get();

        assertThat(line.getName()).isEqualTo(name);
        assertThat(line.getColor()).isEqualTo(color);
    }

    @DisplayName("노선 조회 테스트")
    @Test
    void findById() {
        Long id = 1L;
        Line line = lineDao.findById(id).get();

        assertThat(line.getId()).isEqualTo(id);
    }

    @DisplayName("노선 삭제 테스트")
    @Test
    void delete() {
        Line line = new Line("3호선", "주황색");
        Line savedLine = lineDao.save(line);
        lineDao.delete(savedLine.getId());

        assertThat(lineDao.findAll()).hasSize(1);
    }
}