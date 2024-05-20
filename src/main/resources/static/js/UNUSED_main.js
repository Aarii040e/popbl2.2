// Código dark y light mode

if (localStorage.getItem("mode") == null) localStorage.setItem("mode", "dark");

let iconDiv = document.querySelector(".modeBtn");
let modeIcon = document.querySelector(".faMode");

addEventListener("load", () => {
    (localStorage.getItem("mode") == "dark") ? setMode("dark") : setMode("light");
    (localStorage.getItem("show") == "true") ? setIconVisibility("true") : setIconVisibility("false");
});

iconDiv.addEventListener("click", () => {
    (localStorage.getItem("mode") == "dark") ? setMode("light") : setMode("dark");
    setNavBackground();
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
let icon = document.querySelector(".fixedButton");

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

// Código dropdown

let dropdown = document.getElementById("dropdown");
let subDropdown = document.getElementById("sub-dropdown");
let caret = document.querySelector(".fa");
let loaded = false;

dropdown.addEventListener("click", ()=> {
    if (!loaded) {
        subDropdown.style.display = "block";
        subDropdown.classList.add("shown");
        caret.classList.add("up");
        loaded = true;
    }
    else {
        subDropdown.classList.toggle("hidden");
        subDropdown.classList.toggle("shown");
        caret.classList.toggle("down");
        caret.classList.toggle("up");
    }
});

// Código opacidad nav

let nav = document.querySelector("nav");
let contentStart = Math.floor(scrollY + document.querySelector(".content-start").getBoundingClientRect().top);

const setNavBackground = () => {
    let resultado = scrollY / contentStart;
    let resultadoSubDropdown = resultado + 0.3;
    if (resultado > 1) resultado = 1;
    if (resultadoSubDropdown > 1) resultadoSubDropdown = 1;

    if (window.innerWidth >= 800) {
        if (localStorage.getItem("mode") == "dark") {
            nav.style.background = `rgba(22, 26, 29, ${resultado})`;
            subDropdown.style.background = `rgba(22, 26, 29, ${resultadoSubDropdown})`;
        }

        else if (localStorage.getItem("mode") == "light") {
            nav.style.background = `rgba(68, 136, 238, ${resultado})`;
            subDropdown.style.background = `rgba(68, 136, 238, ${resultadoSubDropdown})`;
        }

    } else {
        nav.style.background = "var(--nav-bg)";
        subDropdown.style.background = "var(--nav-bg)";
    }
}

addEventListener("scroll", () => {
    setNavBackground();
});

addEventListener("load", ()=> {
    subDropdown.style.display = "none";
    setNavBackground();
    setCardHeight();
});

addEventListener("resize", () => {
    contentStart = Math.floor(scrollY + document.querySelector(".content-start").getBoundingClientRect().top);
    setNavBackground();
})