// ini buat buka panel notif
function openNotif() {
    const panel = document.getElementById("notif-panel");
    const overlay = document.getElementById("notif-overlay");

    if (!panel || !overlay) {
        console.error("Panel notif tidak ditemukan!");
        return;
    }

    panel.classList.add("open");
    overlay.style.display = "block";
}

// ini buat tutp panel notif
function closeNotif() {
    const panel = document.getElementById("notif-panel");
    const overlay = document.getElementById("notif-overlay");

    if (!panel || !overlay) {
        console.error("Panel notif tidak ditemukan!");
        return;
    }

    panel.classList.remove("open");
    overlay.style.display = "none";
}

// ini buat tutup panel notif tapi kalo klik nya di overlay nya
document.addEventListener("DOMContentLoaded", () => {
    const overlay = document.getElementById("notif-overlay");
    if (overlay) {
        overlay.addEventListener("click", closeNotif);
    }
});
