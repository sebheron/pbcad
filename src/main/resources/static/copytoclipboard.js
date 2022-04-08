const copy_button = document.getElementById("copy-button");
const console = document.getElementById("console");

function copyToClipboard() {
    var range = document.createRange();
    range.selectNode(console); //changed here
    window.getSelection().removeAllRanges();
    window.getSelection().addRange(range);
    document.execCommand("copy");
    window.getSelection().removeAllRanges();
}

function start() {
    copy_button.addEventListener("click", copyToClipboard);
}

document.addEventListener("DOMContentLoaded", start, false);