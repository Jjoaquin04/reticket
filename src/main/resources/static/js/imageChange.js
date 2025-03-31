const images = [
    "../static/img/cantantes/QuevedoConGafas.webp",
    "../static/img/festivales/festivalCocaCola.webp",
    "../static/img/teatros/teatroLara.webp",
    "../img/cantantes/QuevedoBanner.webp",
    "../img/festivales/festivalCocaCola.webp",
    "../img/teatros/teatroLara.webp",
]


let indiceActual = 0;

function changeImage(direction) {
    indiceActual = (indiceActual + direction + images.length) % images.length;
    document.getElementById("slide-image").src = images[indiceActual];
}

window.onload = function() {
    document.getElementById("slide-image").src = images[0];
}