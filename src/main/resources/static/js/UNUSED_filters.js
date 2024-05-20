// Código esconder/enseñar filtros

let filters = document.querySelector(".filters");
let button = document.querySelector(".roundedFixedBtn");

button.addEventListener("click", () => {
    filters.classList.toggle("hidden");
    button.classList.toggle("hiddenButton");
    filters.style.transition = "right .5s ease-out";
    button.style.transition = "right .5s ease-out";
});

addEventListener("resize", () => {
    filters.style.transition = "none";
    button.style.transition = "none";
});

// Código deshabilitar/habilitar checkbox

const updateCheckbox = value => {
    document.querySelectorAll("input[type=checkbox]").forEach(checkbox => {
        if (value == "all") checkbox.disabled = false;
        else {
            if (value == checkbox.getAttribute("data-category")) {
                checkbox.disabled = false;
            } else {
                checkbox.checked = false;
                checkbox.disabled = true;
            }
        }
    });
}

let category = document.getElementById("choose-category");
category.addEventListener("change", () => updateCheckbox(category.value));