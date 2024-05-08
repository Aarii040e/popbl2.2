// Código sign up

let signUp = document.getElementById("bSignUp");

let result = document.querySelector(".result");

signUp.addEventListener("click", (e)=>{
    e.preventDefault();
    /*let error = validateSignUp();
    result.classList.remove("red");

    if (error[0]) {
        result.textContent = error[1];
        result.classList.add("red");
    }
    else {
        location.href = "index.html";
    }*/

    document.querySelector("form").submit();
    location.href = "index.html";
});

/*const validateSignUp = ()=> {
    let error = [];
    error[0] = true;

    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let confirm = document.getElementById("confirm").value;

    if (name == "") name = null;

    if (!validateEmail(email)) {
        error[1] = "Email address is invalid."
        return error;
    }
    if (username.length < 3 || username.length > 20) {
        error[1] = "Username must be between 3 and 20 characters long."
        return error;
    }
    else if (password.length < 8) {
        error[1] = "Password must be at least 8 characters long.";
        return error;
    }
    else if (password !== confirm) {
        error[1] = "Passwords must be the same.";
        return error;
    }
    
    error[0] = false;
    return error;
}

function validateEmail(value) {
    var input = document.createElement('input');
  
    input.type = 'email';
    input.required = true;
    input.value = value;
  
    return typeof input.checkValidity === 'function' ? input.checkValidity() : /\S+@\S+\.\S+/.test(value);
}*/