var darkmode = false;

const page_root = document.querySelector(":root");
const darkmode_button = document.getElementById('darkmode-button');

function enableDarkMode(on) {
    if (on) {
        page_root.style.setProperty('--page-bg-color', '#0f0905');
        page_root.style.setProperty('--page-shadow', 'black');
        page_root.style.setProperty('--page-output-color', 'mintcream');
        page_root.style.setProperty('--page-output-bg', '#202024');
        page_root.style.setProperty('--page-output-border', '#606060');
        page_root.style.setProperty('--tb-background-color', 'black');
        page_root.style.setProperty('--tb-placeholder-color', 'lightgrey');
        page_root.style.setProperty('--b-color', 'white');
    }
    else {
        page_root.style.setProperty('--page-bg-color', 'mintcream');
        page_root.style.setProperty('--page-shadow', 'darkslategrey');
        page_root.style.setProperty('--page-output-color', '#0f0905');
        page_root.style.setProperty('--page-output-bg', '#e9e9f7');
        page_root.style.setProperty('--page-output-border', 'darkslategrey');
        page_root.style.setProperty('--tb-background-color', 'white');
        page_root.style.setProperty('--tb-placeholder-color', 'darkgrey');
        page_root.style.setProperty('--b-color', 'black');
    }
    darkmode = on;
}

function switchDarkMode() {
    enableDarkMode(!darkmode);
    window.localStorage.setItem('pbcad-darkmode', darkmode);
}

function start() {
    enableDarkMode(window.localStorage.getItem('pbcad-darkmode') === 'true');
    darkmode_button.addEventListener("click", switchDarkMode);
    document.body.classList.remove('preload')
}

document.addEventListener("DOMContentLoaded", start, false);