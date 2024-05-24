// Código habilitar/deshabilitar cambios en el perfil

let edit = document.querySelector(".edit");
let apply = document.querySelector(".apply");

let pfp = document.querySelector(".pfp");
let description = document.querySelector("input[type='text']");

let input = document.createElement("input");
input.type = "file";
input.id = "file";
input.accept = "image/*";

const enableChanges = () => {
    description.disabled = false;
    pfp.classList.add("editable");

    pfp.appendChild(input);
    pfp.addEventListener("click", () => input.click());
}

edit.addEventListener("click", e => {   
    e.preventDefault();
    enableChanges();

    edit.classList.add("d-none");
    apply.classList.remove("d-none");
});

apply.addEventListener("click", e => {
    e.preventDefault();

    if (document.querySelector(".role p").textContent.includes("Volunteer")) {
        document.querySelector("form").action = "/volunteer-view/user/update";
    }

    document.querySelector("form").submit();
});