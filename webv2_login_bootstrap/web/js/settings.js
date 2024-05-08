// Código links aside

document.querySelectorAll(".link").forEach(link => {
    link.addEventListener("click", () => {
        [...document.querySelectorAll(".active")].map(link => link.classList.remove("active"));
        link.classList.add("active");
    });
});

// Código log off

const deleteCookie = (name) => {
    document.cookie = `user=${name};expires=Thu, 1 Jan 1970 00:00:00 UTC}`;
}

let b = document.getElementById("bLogOff");

b.addEventListener("click", () => {
    deleteCookie("user");
    location.href = "index.html";
});

// Código update / apply changes

let bUpdate = document.querySelector(".update");

bUpdate.addEventListener("click", () => {
    if (bUpdate.innerHTML == "Update password") {
        bUpdate.innerHTML = "Apply changes";
        document.querySelector(".info").disabled = false;
    }
    else document.getElementById("updatePassword").submit();
});

// Código color theme

addEventListener("load", () => {
    if (localStorage.getItem("show") == "true") document.getElementById("showIcon").checked = true;
    else document.getElementById("showIcon").checked = false;

    if (localStorage.getItem("mode") == "dark") document.querySelector("select").options[0].selected = true;
    else document.querySelector("select").options[1].selected = true;
});

document.getElementById("bSetTheme").addEventListener("click", () => {
    let selection = document.querySelector("select").value;
    let show = document.getElementById("showIcon");

    setMode(selection);
    (show.checked) ? setIconVisibility("true") : setIconVisibility("false");
});

// Código delete account

document.querySelector(".delete").addEventListener("click", () => confirm("Are you certain you wish to delete your account?"));