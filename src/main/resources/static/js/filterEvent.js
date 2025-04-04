document.addEventListener("DOMContentLoaded",function() {
    const urlParams = new URLSearchParams(window.location.search);
    const eventType = urlParams.get("eventType");
    const location = urlParams.get("location");
    const startDate = urlParams.get("startDate");
    const endDate = urlParams.get("endDate");
    const searchString = urlParams.get("searchString");

    if(eventType != null)   {
        document.getElementById("event-type").value = eventType;
    }
    if(location != null)    {
        document.getElementById("location").value = location;
    }
    if(startDate != null)   {
        document.getElementById("start-date").value = startDate;
    }
    if(endDate != null)     {
        document.getElementById("end-date").value = endDate;
    }
    if(searchString != null) {
        document.getElementById("input-text").value = searchString;
    }
});