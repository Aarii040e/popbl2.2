// Código deshabilitar/habilitar radio & decir categoría

let container = document.querySelector(".cont");

const setCategory = value => {
    container.innerHTML = `<div class="task ${value}Bg d-flex justify-content-between p-2 rounded">
                                <span>Category: ${value}</span>
                                <span class="x">&times</span>
                            </div>`;

    document.querySelector(".x").addEventListener("click", () => container.innerHTML = '');
}

const updateRadio = value => {
    document.querySelectorAll("input[type=radio]").forEach(radio => {
        radio.checked = false;
        if (value == radio.getAttribute("data-type")) {
            radio.parentElement.style.display = "block";

            radio.addEventListener("change", () => {
                if (radio.checked) setCategory(radio.getAttribute("data-category"));
            });
        }
        else radio.parentElement.style.display = "none";
    });
}

let type = document.getElementById("type");
addEventListener("load", () => updateRadio(type.value));
type.addEventListener("change", () => {
    container.innerHTML = '';
    updateRadio(type.value)}
);