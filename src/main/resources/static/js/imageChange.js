const images = [
    "../img/cantantes/QuevedoBanner.webp",
    "../img/cantantes/AnnuelAABanner.webp",
    "../img/cantantes/BadBunnyBanner.webp",
];

let indiceActual = 0;

function changeImage(direction) {
    indiceActual = (indiceActual + direction + images.length) % images.length;
    document.getElementById("slide-image").src = images[indiceActual];
}

window.onload = function() {
    document.getElementById("slide-image").src = images[0];

    // Cambiar imagen automáticamente cada 5 segundos
    setInterval(() => {
        changeImage(1); // Avanzar a la siguiente imagen
    }, 5000);
};