package wooteco.subway.exception.duplicate;

public class DuplicateSectionException extends DuplicateException {
    private static final String MESSAGE = "같은 구간이 존재합니다.";

    public DuplicateSectionException() {
        super(MESSAGE);
    }
}