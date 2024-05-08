// Código cards

const setCardHeight = () => {
    let max = Math.max(visualViewport.height, visualViewport.width) * 0.2;

    document.querySelectorAll(".card, .card-front, .card-back").forEach(card => {
        card.style.width = `${max}px`;
        card.style.height = `${max}px`;
    });
}

addEventListener("resize", () => setCardHeight());
addEventListener("load", () => setCardHeight());