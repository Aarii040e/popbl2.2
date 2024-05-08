// Código dark y light mode

if (localStorage.getItem("mode") == null) localStorage.setItem("mode", "dark");

let iconDiv = document.querySelector(".modeBtn");
let modeIcon = document.querySelector(".faMode");
let nav = document.querySelector("nav");

addEventListener("load", () => {
    (localStorage.getItem("mode") == "dark") ? setMode("dark") : setMode("light");
    (localStorage.getItem("show") == "true") ? setIconVisibility("true") : setIconVisibility("false");
});

iconDiv.addEventListener("click", () => {
    (localStorage.getItem("mode") == "dark") ? setMode("light") : setMode("dark");
});

const setMode = mode => {
    if (mode == "light") {
        localStorage.setItem("mode", "light");
        modeIcon.src = "img/moon.png";
        modeIcon.alt = "Moon";
        document.body.classList.add("light-mode");
    }
    else if (mode == "dark") {
        localStorage.setItem("mode", "dark");
        modeIcon.src = "img/sun.png";
        modeIcon.alt = "Sun";
        document.body.classList.remove("light-mode");
    }
}

// Código ocultar icons sol y luna

if (localStorage.getItem("show") == null) localStorage.setItem("show", "true");
let icon = document.querySelector(".modeBtn");

const setIconVisibility = show => {
    if (show == "true") {
        localStorage.setItem("show", "true");
        icon.classList.remove("hide");
    }
    else if (show == "false") {
        localStorage.setItem("show", "false");
        icon.classList.add("hide");
    }
}

// Código botón close

let closeDiv = document.querySelector(".fixedButtonTop");
let closeIcon = document.querySelector(".faIcon");

if (closeDiv) {
    closeDiv.addEventListener("click", () => {
        location.href = "index.html";
    });
}