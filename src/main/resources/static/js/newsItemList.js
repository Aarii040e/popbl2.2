document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.truncated-content').forEach((element) => {
        let fullText = element.textContent;
        element.textContent = truncateText(fullText, 30);
    });
});

function truncateText(text, maxWords) {
    let words = text.split(' ');
    if (words.length > maxWords) {
        return words.slice(0, maxWords).join(' ') + '...';
    }
    console.log(text);
    return text;
}
