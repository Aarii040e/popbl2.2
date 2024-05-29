const cut = (el, char) => {
    document.querySelectorAll(el).forEach(element => {
        if (element.innerText.length > char) {
            let oldText = element.textContent;
            let newText = element.textContent.substring(0, char);
            
            let dots = document.createElement("a");
            dots.classList.add("link");
            dots.textContent = "... Show more";
    
            element.textContent = newText;
            element.appendChild(dots);
    
            dots.addEventListener("click", () => {
                if (dots.textContent == "... Show more") {
                    element.textContent = oldText;
                    dots.style.marginLeft = "5px";
                    element.appendChild(dots);
                    dots.textContent = "Show less";
                } else {
                    element.textContent = newText;
                    dots.style.marginLeft = "0";
                    element.appendChild(dots);
                    dots.textContent = "... Show more";
                }
            });
        }
    });   
}