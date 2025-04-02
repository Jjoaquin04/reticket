const images = [
    "../img/cantantes/QuevedoBanner.webp",
    "../img/cantantes/AnnuelAABanner.webp",
    "../img/cantantes/BadBunnyBanner.webp",
];

let indiceActual = 0;
let intervalId;

function changeImage(direction) {
    indiceActual = (indiceActual + direction + images.length) % images.length;
    document.getElementById("slide-image").src = images[indiceActual];
    resetInterval();
}
function resetInterval()    {
    clearInterval(intervalId);
    intervalId = setInterval(() => {
        changeImage(1)
    }, 5000);
}

window.onload = function() {
    document.getElementById("slide-image").src = images[0];
    resetInterval();
};