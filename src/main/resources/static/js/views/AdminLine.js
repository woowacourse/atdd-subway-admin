import { EVENT_TYPE } from "../../utils/constants.js";
import {
    colorSelectOptionTemplate,
    subwayLineInfoTemplate,
    subwayLinesTemplate
} from "../../utils/templates.js";
import { subwayLineColorOptions } from "../../utils/defaultSubwayData.js";
import Modal from "../../ui/Modal.js";
import Api from "../../api/index.js";

function AdminLine() {
    const $subwayLineList = document.querySelector("#subway-line-list");
    const $subwayLineNameInput = document.querySelector("#subway-line-name");
    const $subwayLineColorInput = document.querySelector("#subway-line-color");
    const $subwayLineFirstTimeInput = document.querySelector("#first-time");
    const $subwayLineLastTimeInput = document.querySelector("#last-time");
    const $subwayLineIntervalTimeInput = document.querySelector("#interval-time");
    const $subwayLineInfoContainer = document.querySelector("#subway-line-info-container");

    const $submitSubwayLineButton = document.querySelector(
        "#subway-line-create-form #submit-button"
    );
    const subwayLineModal = new Modal();

    let updateLineId = null;

    const onCreateSubwayLine = lineRequest => {
        Api.line.create(lineRequest)
            .then((response) => response.json())
            .then((data) => {
                const selectedSubwayLine = {
                    id: data.id,
                    name: data.name,
                    color: data.color
                };

                $subwayLineList.insertAdjacentHTML(
                    "beforeend",
                    subwayLinesTemplate(selectedSubwayLine)
                );

                const selectedSubwayInfo = {
                    startTime: data.startTime,
                    endTime: data.endTime,
                    intervalTime: data.intervalTime
                };

                $subwayLineInfoContainer.innerHTML = subwayLineInfoTemplate(selectedSubwayInfo);
            })
            .then(() => {
                $subwayLineNameInput.value = "";
                $subwayLineColorInput.value = "";
                $subwayLineFirstTimeInput.value = "";
                $subwayLineLastTimeInput.value = "";
                $subwayLineIntervalTimeInput.value = "";
                subwayLineModal.toggle();
            });
    };

    const onReadSubwayLine = event => {
        const $target = event.target;
        const isNameDiv = $target.classList.contains("line-name");
        if (isNameDiv) {
            const lineId = $target.closest(".subway-line-item").querySelector(".line-id").textContent;
            Api.line.getDetail(lineId)
                .then((response) => response.json())
                .then((data) => {
                    const selectedSubwayInfo = {
                        startTime: data.startTime,
                        endTime: data.endTime,
                        intervalTime: data.intervalTime
                    };
                    $subwayLineInfoContainer.innerHTML = subwayLineInfoTemplate(selectedSubwayInfo);
                })
        }
    };

    const onUpdateSubwayLine = (lineRequest) => {
        console.log(lineRequest);
        Api.line.update(lineRequest, updateLineId)
            .then((response) => response.json())
            .then((data) => {
                const selectedSubwayLine = {
                    id: data.id,
                    name: data.name,
                    color: data.color
                };

                $subwayLineList.innerHTML = "";
                initDefaultSubwayLines();
                //todo : 추후 변경된 값만 삭제하는 로직 구현 목표

                const selectedSubwayInfo = {
                    startTime: data.startTime,
                    endTime: data.endTime,
                    intervalTime: data.intervalTime
                };

                $subwayLineInfoContainer.innerHTML = subwayLineInfoTemplate(selectedSubwayInfo);
            })
            .then(() => {
                $subwayLineNameInput.value = "";
                $subwayLineColorInput.value = "";
                $subwayLineFirstTimeInput.value = "";
                $subwayLineLastTimeInput.value = "";
                $subwayLineIntervalTimeInput.value = "";
                subwayLineModal.toggle();
            });
    };

    const onDeleteSubwayLine = event => {
        const $target = event.target;
        const isDeleteButton = $target.classList.contains("mdi-delete");
        if (isDeleteButton) {
            const lineId = $target.closest(".subway-line-item").querySelector(".line-id").textContent;
            Api.line.delete(lineId);
            $target.closest(".subway-line-item").remove();
        }
    };

    const onEditSubwayLine = event => {
        const $target = event.target;
        const isUpdateButton = $target.classList.contains("mdi-pencil");
        if (isUpdateButton) {
            updateLineId = $target.closest(".subway-line-item").querySelector(".line-id").textContent;
            subwayLineModal.toggle();
        }
    };

    const onSubmitSubwayLine = event => {
        event.preventDefault();

        const lineRequest = {
            name: $subwayLineNameInput.value,
            startTime: $subwayLineFirstTimeInput.value,
            endTime: $subwayLineLastTimeInput.value,
            intervalTime: $subwayLineIntervalTimeInput.value,
            color: $subwayLineColorInput.value
        };

        if (updateLineId != null) {
            onUpdateSubwayLine(lineRequest);
            updateLineId = null;
        }else {
            onCreateSubwayLine(lineRequest);
        }
    };

    const initDefaultSubwayLines = () => {
            Api.line.get()
                .then((response) => response.json())
                .then((data) => {
                    data.map(line => {
                        $subwayLineList.insertAdjacentHTML(
                            "beforeend",
                            subwayLinesTemplate(line));
                    })
                })
        }
    ;

    const initEventListeners = () => {
            $subwayLineList.addEventListener(EVENT_TYPE.CLICK, onDeleteSubwayLine);
            $subwayLineList.addEventListener(EVENT_TYPE.CLICK, onEditSubwayLine);
            $subwayLineList.addEventListener(EVENT_TYPE.CLICK, onReadSubwayLine);
            $submitSubwayLineButton.addEventListener(
                EVENT_TYPE.CLICK,
                onSubmitSubwayLine
            );
        }
    ;

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
                .map((option, index) => colorSelectOptionTemplate(option, index)
                )
                .join("");
            $colorSelectContainer.insertAdjacentHTML("beforeend", colorSelectTemplate);
            $colorSelectContainer.addEventListener(
                EVENT_TYPE.CLICK,
                onSelectColorHandler
            );
        }
    ;

    this.init = () => {
        initDefaultSubwayLines();
        initEventListeners();
        initCreateSubwayLineForm();
    }
    ;
}

const adminLine = new AdminLine();
adminLine.init();