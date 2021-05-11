package wooteco.subway.line.dao;

import org.springframework.util.ReflectionUtils;
import wooteco.subway.domain.Line;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryLineDao implements LineDao {

    private Long seq = 0L;
    private List<Line> lines = new ArrayList<>();

    private Line createNewObject(Line line) {
        Field field = ReflectionUtils.findField(Line.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, line, ++seq);
        return line;
    }

    @Override
    public Line create(Line line) {
        Line createdLine = createNewObject(line);
        lines.add(createdLine);
        return createdLine;
    }

    @Override
    public boolean existByInfo(String name, String color) {
        return lines.stream()
                .anyMatch(line -> line.isSameName(name) || line.isSameColor(color));
    }

    @Override
    public Line findById(Long lineId) {
        return lines.stream()
                .filter(line -> line.isSameId(lineId))
                .findAny()
                .get();
    }

    @Override
    public boolean existById(Long lineId) {
        return lines.stream()
                .anyMatch(line -> line.isSameId(lineId));
    }

    @Override
    public List<Line> showAll() {
        return new ArrayList<>(lines);
    }
}
