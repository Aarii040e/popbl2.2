// Código show more/less

document.querySelectorAll(".description").forEach(desc => {
    if (desc.textContent.length > 120) {
        let oldText = desc.textContent;
        let newText = desc.textContent.substring(0, 120);
        
        let dots = document.createElement("a");
        dots.classList.add("link");
        dots.textContent = "... Show more";

        desc.textContent = newText;
        desc.appendChild(dots);

        dots.addEventListener("click", () => {
            if (dots.textContent == "... Show more") {
                desc.textContent = oldText;
                dots.style.marginLeft = "5px";
                desc.appendChild(dots);
                dots.textContent = "Show less";
            } else {
                desc.textContent = newText;
                dots.style.marginLeft = "0";
                desc.appendChild(dots);
                dots.textContent = "... Show more";
            }
        });
    }
});