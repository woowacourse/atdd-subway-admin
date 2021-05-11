package wooteco.subway.line.dao;

import wooteco.subway.domain.Line;

import java.util.List;
import java.util.Optional;

public interface LineDao {
    Line save(Line line);

    boolean existByInfo(String name, String color);

    Line findById(Long lineId);
    boolean existById(Long lineId);

    List<Line> showAll();
}
