# 지하철 노선 관리 기능

## 레벨 1

### 요구 사항

- 인수 테스트(LineAcceptanceTest) 성공 시키기
- LineController를 구현하고 인수 테스트에 맞는 기능을 구현하기
- 테스트의 중복을 제거하기

### 기능 목록

1. 지하철 노선 추가 API

2. 지하철 노선 목록 조회 API

3. 지하철 노선 수정 API

4. 지하철 노선 단건 조회 API

5. 지하철 노선 제거 API



### 시나리오
~~~
Feature: 지하철 노선 관리 
    Scenario: 지하철 노선을 관리한다. 
    When 지하철 노선 n개 추가 요청을 한다. 
    Then 지하철 노선이 추가 되었다. 
    
    When 지하철 노선 목록 조회 요청을 한다. 
    Then 지하철 노선 목록을 응답 받는다. 
    And 지하철 노선 목록은 n개이다. 
    
    When 지하철 노선 수정 요청을 한다. 
    Then 지하철 노선이 수정 되었다. 
    
    When 지하철 노선 제거 요청을 한다. 
    Then 지하철 노선이 제거 되었다. 
    
    When 지하철 노선 목록 조회 요청을 한다. 
    Then 지하철 노선 목록을 응답 받는다. 
    And 지하철 노선 목록은 n-1개이다.
~~~

### 프로그래밍 제약 사항

- 지하철 노선 이름은 중복될 수 없다.

### 미션 수행 순서

- 인수 조건 파악하기 (제공)
- 인수 테스트 작성하기 (제공)
- 인수 테스트 성공 시키기
- 기능 구현


## 레벨 2

### 요구 사항
인수 테스트를 통해 구현한 기능을 페이지에 연동하기


### 기능 목록
**지하철 노선 관리 페이지**
- 페이지 호출 시 미리 저장한 지하철 노선 조회  
- 지하철 노선 목록 조회 API 사용  
**노선 추가**  
- 노선 추가 버튼을 누르면 아래와 같은 팝업화면이 뜸  
- 노선 이름과 정보를 입력  
- 지하철 노선 추가 API 사용

**노선 상세 정보 조회**
- 목록에서 노선 선택 시 상세 정보를 조회

**노선 수정**
- 목록에서 우측 수정 버튼을 통해 수정 팝업화면 노출
- 수정 팝업 노출 시 기존 정보는 입력되어 있어야 함
- 정보 수정 후 지하철 노선 수정 API 사용

**노선 삭제**
- 목록에서 우측 삭제 버튼을 통해 삭제
- 지하철 노선 삭제 API 사용

## 레벨 3(노선별 지하철역 관리노선별 지하철역 관리)

### 요구 사항

- 인수 테스트(LineStationAcceptanceTest)를 완성 시키기
- Mock 서버와 DTO 만 정의하여 테스트를 성공 시키기
    - 기능 구현은 다음 단계에서 진행
- 기존에 구현한 테스트들과의 중복을 제거하기

### 기능 목록

**지하철 노선에 역 추가**

- 노선에 지하철 역이 추가될 경우 아래의 정보가 추가되어야 함
    - 이전역과의 **`거리`**
    - 이전역과의 **`소요시간`**
- DTO 예시

```
public class LineStationCreateRequest {
    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;
    ...

```

**지하철 노선에 역 제거**

- 노선과 제거할 지하차철역 식별값을 전달

### 시나리오

```
Feature: 지하철 노선에 역 추가 / 제거

Scenario: 지하철 노선에 역을 추가하고 제거한다.
     Given 지하철역이 여러 개 추가되어있다.
     And 지하철 노선이 추가되어있다.

     When 지하철 노선에 지하철역을 등록하는 요청을 한다.
     Then 지하철역이 노선에 추가 되었다.

     When 지하철 노선의 지하철역 목록 조회 요청을 한다.
     Then 지하철역 목록을 응답 받는다.
     And 새로 추가한 지하철역을 목록에서 찾는다.

     When 지하철 노선에 포함된 특정 지하철역을 제외하는 요청을 한다.
     Then 지하철역이 노선에서 제거 되었다.

     When 지하철 노선의 지하철역 목록 조회 요청을 한다.
     Then 지하철역 목록을 응답 받는다.
     And 제외한 지하철역이 목록에 존재하지 않는다.
```

## 4단계 - 노선별 지하철역 / 로직

**노선별 지하철역 관리 기능 구현하기**

### 요구사항

**LineServiceTest 테스트 성공 시키기**

```
@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    ...

    @Test
    void addLineStationAtTheFirstOfLine() {
        LineStationCreateRequest request = new LineStationCreateRequest(null, 4L, 10, 10);

        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations()).hasSize(4);
        assertThat(line.getLineStationsId().get(0)).isEqualTo(4L);
        assertThat(line.getLineStationsId().get(1)).isEqualTo(1L);
        assertThat(line.getLineStationsId().get(2)).isEqualTo(2L);
        assertThat(line.getLineStationsId().get(3)).isEqualTo(3L);
    }
    
    ...

```

**LineTest 테스트 성공 시키기**

```
public class LineTest {
    private Line line;

    @Test
    void getLineStations() {
        List<Long> stationIds = line.getLineStationsId();

        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
    }
    ...
}

```

### 기능목록

> 기능 제약조건한 노선의 출발역은 하나만 존재하고 단방향으로 관리함실재 운행 시 양쪽 두 종점이 출발역이 되겠지만 관리의 편의를 위해 단방향으로 관리추후 경로 검색이나 시간 측정 시 양방향을 고려 할 예정한 노선에서 두 갈래로 갈라지는 경우는 없음이전역이 없는 경우 출발역으로 간주

### 지하철 노선에 역 추가

- 마지막 역이 아닌 뒷 따르는 역이 있는경우 재배치를 함
    - 노선에 A - B - C 역이 연결되어 있을 때 B 다음으로 D라는 역을 추가할 경우 A - B - D - C로 재배치 됨


### 지하철 노선에 역 제거

- 출발역이 제거될 경우 출발역 다음으로 오던 역이 출발역으로 됨
- 중간역이 제거될 경우 재배치를 함
    - 노선에 A - B - D - C 역이 연결되어 있을 때 B역을 제거할 경우 A - B - C로 재배치 됨

## 5단계 - 노선별 지하철역 / 페이지

**노선별 지하철역 관리 페이지 연동**

### 요구 사항

- 지하철 구간 관리 페이지 연동하기
- **앞 단계에서 구현되지 않은 기능이라 하더라도 필요시 구현해서 사용 가능**

### 기능 목록

**구간 페이지 연동**

- 전체 노선 목록과 노선에 등록된 지하철역 목록을 통해 페이지 로드
- 지하철역 목록을 조회하는 방법은 자유롭게 선택 가능(제약을 두지 않음)
    - 최초 페이지 로드 시 모든 정보를 포함하는 방법
    - 지하철 노선 선택 시 해당 노선의 지하철역 목록 조회하는 방법

**구간 추가**

- 추가 버튼과 팝업화면을 통해 추가

**구간 제거**

- 목록 우측 제거 버튼을 통해 제거

# 이번 미션을 통해 도달한 내 입장들.

 - view <-> controller <-> service 간에는 DTO를 사용해야한다.
 
 - controller <-> service 사이는 최대한 도메인을 간단하게 감싼 DTO로 값을
  주고 받되 Request, Response라는 이름을 가진 view와 직접적으로 연관이 있는
  DTO가 들어가지 못하는 것은 아니다.
  - 즉, 뷰와 직접적으로 관계있는 DTO도 Service에 침투해도 된다.
  - 결론적으로 도메인 객체가 레이어 이동을 하는 것은 기피한다.
   - 그렇게 생각하는 이유는 도메인의 변경은 오직 Service에서만 관리하여
    **Controller가 도메인과 관련된 비즈니스 로직에 최대한 관여하지 못하게**하는 것과 
    최대한 **뷰 DTO와 Service DTO간의 직접적인 연결을 제거**하여 뷰의 변화가 
    Controller까지만 영향을 미치게끔 하는 것.
    
 - 동급의 Layer가 동급의 Layer를 사용하는 것은 문제 없다. Service간의 연관도 가능하다는 의미.
 
 - Line과 LineStation이 연결되있지만 LineStation은 Station과 Line의 관계테이블이므로
  굳이 LineStationService를 두지 않았다. 그 이유는 LineStation은 오직 Line으로만 관리할 수
  있기 때문. 이 관계를 파악하기 위해서는 Line이 삭제되면 LineStation이 삭제되어야하는 지와
  같이 A의 삭제가 B의 영향을 주는 **종속 관계**인지를 체크하면 된다. 앞으로는 종속 관계인지를 기준으로
  Service를 분리해보자.
  
 - ControllerAdvice 혹은 ExceptionHandler로 현명하게 예외처리를 해보자.
 
 - Valid로 여러 유효성 검사를 쉽게 할 수 있다.
  
