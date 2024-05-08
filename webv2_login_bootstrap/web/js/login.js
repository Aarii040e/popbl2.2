// Código log in

let logIn = document.getElementById("bLogIn");

let result = document.createElement("p");
result.classList.add("result");
document.querySelector("form").appendChild(result);
result = document.querySelector(".result");

let checkbox = document.getElementById("rememberme");
let valid = false;

logIn.addEventListener("click", e => {
    e.preventDefault();
    /*let error = validateLogIn();

    if (error[0]) {
        result.textContent = error[1];
        result.classList.add("red");
    }
    else {*/
    if (document.getElementById("username").value == "user" && document.getElementById("password").value == "password") {
        if (checkbox.checked == true) createCookies();
        location.href = "adverts.html";
    }
    //}
});
/*
const validateLogIn = ()=> {
    let error = [];
    error[0] = true;
    valid = false;

    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    if (username != "user" || password !== "password") {
        error[1] = "Username or password is incorrect.";
        return error;
    }
    
    valid = true;
    error[0] = false;
    return error;
}*/

// Código remember me

checkbox.addEventListener("change", e => {
    validateLogIn();
    if (e.target.checked && valid) {
        createCookies();
    }
});

const calcDateUTC = days => {
    let date = new Date();
    date.setTime(date.getTime() + days*86400000); //86400000ms = 24h * 60min * 60s * 1000ms
    return date;
}

const createCookie = (name, expires) => {
    document.cookie = `${name};expires=${calcDateUTC(expires)}`;
}

const createCookies = () => {
    createCookie(`user=${document.getElementById("username").value}`, 30);
}

const getCookies = name => {
    let cookies = document.cookie;
    cookies = cookies.split(";");

    for (let i=0; i < cookies.length; i++) {
        let cookie = cookies[i].trim();
        if (cookie.startsWith(name)) return cookie.split("=")[1];
    }
}

addEventListener("load", () => {
    if (getCookies("user") != undefined) {
        location.href = "settings.html";
    }
});