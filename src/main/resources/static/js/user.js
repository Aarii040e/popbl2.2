// Código habilitar/deshabilitar cambios en el perfil

let edit = document.querySelector(".edit");
let apply = document.querySelector(".apply");

let pic = document.querySelector(".pic");
let description = document.querySelector("input[type='text']");

const enableChanges = () => {
    description.disabled = false;
    pic.classList.add("editable");
    pic.addEventListener("click", () => document.querySelector(".file").click());
}

edit.addEventListener("click", e => {   
    e.preventDefault();
    enableChanges();

    edit.classList.add("d-none");
    apply.classList.remove("d-none");
});

apply.addEventListener("click", e => {
    e.preventDefault();

    // if (document.querySelector(".role p").textContent.includes("Volunteer")) {
    //     document.querySelector("form").action = "/volunteer-view/user/update";
    // }

    document.querySelector("form").submit();
});

//

cut(".description", 120);