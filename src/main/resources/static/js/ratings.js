// Código stars

document.querySelectorAll(".stars").forEach(star => {
    star.addEventListener("click", e => {
        
        let node = e.target;
        if (!node.classList.contains("star")) return;

        let startNode = node;

        // Se seleccionan la estrella clickada y todas las anteriores
        while (node) {
            node.classList.add("clicked");
            node.classList.add("fa-solid");
            node.classList.remove("fa-regular");
            node = node.previousElementSibling;
        }

        node = startNode.nextElementSibling;

        // Se deseleccionan todas las estrellas siguientes a la clickada
        while (node) {
            node.classList.remove("clicked");
            node.classList.remove("fa-solid");
            node.classList.add("fa-regular");
            node = node.nextElementSibling;
        }
    });
});