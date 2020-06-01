import Modal from "../../ui/Modal.js";
import {EVENT_TYPE} from "../../utils/constants.js";
import api from "../../api/index.js";
import {colorSelectOptionTemplate, subwayLinesTemplate} from "../../utils/templates.js";
import {subwayLineColorOptions} from "../../utils/defaultSubwayData.js";

function AdminLine() {
    const $subwayLineList = document.querySelector("#subway-line-list");
    const $subwayLineNameInput = document.querySelector("#subway-line-title");
    const $subwayLineFirstTimeInput = document.querySelector("#first-time");
    const $subwayLineLastTimeInput = document.querySelector("#last-time");
    const $subwayLineIntervalTimeInput = document.querySelector("#interval-time");
    const $subwayLineColorInput = document.querySelector("#subway-line-color");
    const $subwayLineFormSubmitButton = document.querySelector("#submit-button");
    let $activeSubwayLineItem = null;
    const subwayLineModal = new Modal();
    const onSubmitHandler = (event) => {
        event.preventDefault();
        const $target = event.target;
        const isUpdateSubmit = $target.classList.contains("update-submit-button");
        isUpdateSubmit ? onUpdateSubwayLine(event) : onCreateSubwayLine(event)
    };
    const onViewSubwayInfo = event => {
        const $target = event.target;
        const isSubwayTitle = $target.classList.contains("subway-line-title");
        if (isSubwayTitle) {
            const $id = $target.dataset.lineId;
            api.line.getLineById($id).then(data => {
                    const $subwayLineFirstTime = document.querySelector("#start-subway-time");
                    $subwayLineFirstTime.innerText = data.startTime;
                    const $subwayLineLastTime = document.querySelector("#last-subway-time");
                    $subwayLineLastTime.innerText = data.endTime;
                    const $subwayLineIntervalTime = document.querySelector("#subway-interval-time");
                    $subwayLineIntervalTime.innerText = data.intervalTime + "분";
                }
            );
        }
    };
    const onPencilClicked = event => {
        const $target = event.target;
        const $subwayLineItem = $target.closest(".subway-line-item");
        $activeSubwayLineItem = $subwayLineItem;
        const $submitButton =  document.querySelector('#submit-button');
        const isUpdateButton = $target.classList.contains("mdi-pencil");
        if (!isUpdateButton) {
            return
        }
        const $id = $subwayLineItem.dataset.lineId;
        api.line.getLineById($id).then(data => {
            const $subwayLineTitle = document.getElementById("subway-line-title");
            $subwayLineTitle.value = data.title;
            const $subwayLineFirstTime = document.getElementById("first-time");
            $subwayLineFirstTime.value = data.startTime;
            const $subwayLineLastTime = document.getElementById("last-time");
            $subwayLineLastTime.value = data.endTime;
            const $subwayLineIntervalTime = document.getElementById("interval-time");
            $subwayLineIntervalTime.value = data.intervalTime;
            const $subwayLineColor = document.getElementById("subway-line-color");
            $subwayLineColor.value = data.bgColor;
            subwayLineModal.toggle();
            $submitButton.classList.add('update-submit-button');
        }).catch(() => alert("데이터를 불러올 수 없습니다."));
    };
    const onCreateSubwayLine = event => {
        event.preventDefault();
        const data = {
            name: $subwayLineNameInput.value,
            startTime: $subwayLineFirstTimeInput.value,
            endTime: $subwayLineLastTimeInput.value,
            intervalTime: $subwayLineIntervalTimeInput.value,
            bgColor: $subwayLineColorInput.value
        };
        api.line.create(data)
            .then(subwayLine => {
                $subwayLineList.insertAdjacentHTML(
                    "beforeend",
                    subwayLinesTemplate(subwayLine)
                );
                subwayLineModal.toggle();
            }).catch(() => alert("노선 추가 중 에러가 발생했습니다"));
        $subwayLineNameInput.value = "";
        $subwayLineColorInput.value = "";
        $subwayLineFirstTimeInput.value = "";
        $subwayLineLastTimeInput.value = "";
        $subwayLineIntervalTimeInput.value = "";
    };
    const onDeleteSubwayLine = event => {
        const $target = event.target;
        const isDeleteButton = $target.classList.contains("mdi-delete");
        if (isDeleteButton) {
            const $id = $target.dataset.lineId;
            api.line.delete($id).then();
            $target.closest(".subway-line-item").remove();
            const $subwayLineFirstTime = document.querySelector("#start-subway-time");
            $subwayLineFirstTime.innerText = "";
            const $subwayLineLastTime = document.querySelector("#last-subway-time");
            $subwayLineLastTime.innerText = "";
            const $subwayLineIntervalTime = document.querySelector("#subway-interval-time");
            $subwayLineIntervalTime.innerText = "";
        }
    };
    const onUpdateSubwayLine = event => {
        let lineId = $activeSubwayLineItem.dataset.lineId;
        const data = {
            name: $subwayLineNameInput.value,
            startTime: $subwayLineFirstTimeInput.value,
            endTime: $subwayLineLastTimeInput.value,
            intervalTime: $subwayLineIntervalTimeInput.value,
            bgColor: $subwayLineColorInput.value
        };
        api.line.update(lineId, data).then(line => {
            subwayLineModal.toggle();
            let activeSubwayLineItem = $activeSubwayLineItem.querySelector('.subway-line-title');
            activeSubwayLineItem.innerText = line.title;
        }).catch(() => alert("노선 수정 중 에러가 발생했습니다."));
    };
    const initDefaultSubwayLines = () => {
        api.line.get().then(subwayLines => subwayLines.forEach(
            subwayLine => {
                $subwayLineList.insertAdjacentHTML(
                    "beforeend",
                    subwayLinesTemplate(subwayLine)
                );
            }
        ));
    };
    const initEventListeners = () => {
        $subwayLineList.addEventListener(EVENT_TYPE.CLICK, onDeleteSubwayLine);
        $subwayLineList.addEventListener(EVENT_TYPE.CLICK, onPencilClicked);
        $subwayLineList.addEventListener(EVENT_TYPE.CLICK, onViewSubwayInfo);
        $subwayLineFormSubmitButton.addEventListener(EVENT_TYPE.CLICK, onSubmitHandler);
    };
    const onSelectColorHandler = event => {
        event.preventDefault();
        const $target = event.target;
        if ($target.classList.contains("color-select-option")) {
            document.querySelector("#subway-line-color").value =
                $target.dataset.color;
        }
    };
    const initCreateSubwayLineForm = () => {
        const $colorSelectContainer = document.querySelector(
            "#subway-line-color-select-container"
        );
        const colorSelectTemplate = subwayLineColorOptions
            .map((option, index) => colorSelectOptionTemplate(option, index))
            .join("");
        $colorSelectContainer.insertAdjacentHTML("beforeend", colorSelectTemplate);
        $colorSelectContainer.addEventListener(
            EVENT_TYPE.CLICK,
            onSelectColorHandler
        );
    };
    this.init = () => {
        initDefaultSubwayLines();
        initCreateSubwayLineForm();
        initEventListeners();
    };
}
const adminLine = new AdminLine();
adminLine.init();