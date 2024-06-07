// Código show more/less

cut(".description", 120);

document.querySelectorAll(".bookmark").forEach(bookmark => {
    bookmark.addEventListener("click", () => {
        bookmark.classList.toggle("fa-solid");
        bookmark.classList.toggle("fa-regular");
    });
});