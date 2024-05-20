// Código voluntario

const setRequired = bool => {
    document.querySelectorAll(".optional input").forEach(input => {
        input.required = bool;
        if (bool === false) input.value = null;
    });
}
  
let optional = document.querySelector(".optional");

document.getElementById("volunteer").addEventListener("change", () => {
    optional.classList.toggle("hidden");
    optional.classList.contains("hidden")
        ? setRequired(false)
        : setRequired(true);
});
  