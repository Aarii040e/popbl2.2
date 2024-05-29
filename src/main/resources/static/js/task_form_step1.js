// Código deshabilitar/habilitar radio & decir categoría

let container = document.querySelector(".cont");

const setCategory = value => {
    container.innerHTML = `<div class="task ${value}Bg d-flex justify-content-between p-2 rounded">
                                <span>Category: ${value}</span>
                                <span class="x">&times</span>
                            </div>`;

    document.querySelector(".x").addEventListener("click", () => container.innerHTML = '');
}

document.querySelectorAll("input[type=radio]").forEach(radio => {
    radio.addEventListener("change", () => {
        if (radio.checked) setCategory(radio.getAttribute("data-category"));
    });
});