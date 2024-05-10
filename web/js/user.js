// Código habilitar/deshabilitar cambios en el perfil

let edit = document.querySelector(".edit");
let settings = document.querySelector(".settings");

let banner = document.querySelector(".banner");
let pfp = document.querySelector(".pfp");
let biography = document.querySelector(".biography");

let input = document.createElement("input");
input.type = "file";
input.id = "file";

let input2 = document.createElement("input");
input2.type = "file";
input2.id = "file";

const enableChanges = () => {
    edit.textContent = "Apply changes";

    banner.classList.add("editable");
    pfp.classList.add("editable");
    biography.contentEditable = true;

    banner.appendChild(input);
    pfp.appendChild(input2);
}

const disableChanges = () => {
    edit.textContent = "Edit profile";

    banner.classList.remove("editable");
    pfp.classList.remove("editable");
    biography.contentEditable = false;

    banner.removeChild(input);
    pfp.removeChild(input2);
    
    document.querySelector("form").submit();
}

edit.addEventListener("click", () => {
    if (edit.textContent == "Edit profile") enableChanges();
    else disableChanges();
});

input.addEventListener("change", () => {
    console.log(input.files[0].name);
});