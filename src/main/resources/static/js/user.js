// Código habilitar/deshabilitar cambios en el perfil

let edit = document.querySelector(".edit");
let apply = document.querySelector(".apply");
let settings = document.querySelector(".settings");

let pfp = document.querySelector(".pfp");
let biography = document.querySelector(".biography");

let input = document.createElement("input");
input.type = "file";
input.id = "file";
input.accept = "image/*";

const enableChanges = () => {
    edit.textContent = "Apply changes";

    biography.contentEditable = true;

    pfp.classList.add("editable");
    pfp.appendChild(input);
    pfp.addEventListener("click", () => input.click());
}

const disableChanges = () => {
    edit.textContent = "Edit profile";

    biography.contentEditable = false;

    pfp.classList.remove("editable");
    pfp.removeChild(input);
    
    document.querySelector("form").submit();
}

edit.addEventListener("click", () => {
    enableChanges();
    edit.classList.add("d-none");
    apply.classList.remove("d-none");
});

apply.addEventListener("click", () => {
    disableChanges();
    edit.classList.remove("d-none");
    apply.classList.add("d-none");
});