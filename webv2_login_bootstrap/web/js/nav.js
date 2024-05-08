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

let themeIcon = document.querySelector(".modeBtn");
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
});

addEventListener("resize", () => {
    contentStart = Math.floor(scrollY + document.querySelector(".content-start").getBoundingClientRect().top);
    setNavBackground();
});

themeIcon.addEventListener("click", () => {
    setNavBackground();
});