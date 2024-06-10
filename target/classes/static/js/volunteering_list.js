// Código show more/less

cut(".description", 120);

// Código bookmark

document.querySelectorAll(".bookmark").forEach(bookmark => {
    let currentBookmark = bookmark.getAttribute('id');
    let list = document.querySelector(".taskIDList").innerText.replace(/[\[\]]/g,'').replace(/\s/g,'').split(",");
    if (list.includes(currentBookmark)) {
        bookmark.classList.add("fa-solid");
        bookmark.classList.remove("fa-regular");
        bookmark.href="/volunteer-view/task/" + bookmark.getAttribute('id') + "/delete";
    }
    else {
        bookmark.classList.remove("fa-solid");
        bookmark.classList.add("fa-regular");
        bookmark.href="/volunteer-view/task/" + bookmark.getAttribute('id') + "/save";
    }

    document.querySelectorAll(".signup").forEach(button => bookmark.style.right = `${button.clientWidth + 18}px`);
});