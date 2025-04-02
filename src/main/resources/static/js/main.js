document.addEventListener("DOMContentLoaded", () => {
    const dateElements = document.querySelectorAll(".date-event");
    dateElements.forEach((element) => {
        element.textContent = element.textContent.replace("T", " | ");
    });
});