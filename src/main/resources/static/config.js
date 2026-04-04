/**
 * Resolves the Spring Boot API base URL when the HTML is opened from a different
 * origin (VS Code Live Server, another port, or file://). Same-origin use keeps
 * window.location.origin so requests hit the running app directly.
 *
 * Optional override: <meta name="api-base" content="http://localhost:9090">
 */
(function () {
    var DEFAULT_PORT = "8080";
    var meta = document.querySelector('meta[name="api-base"]');
    if (meta && meta.content && meta.content.trim()) {
        window.API_BASE = meta.content.replace(/\/$/, "");
        return;
    }
    var loc = window.location;
    var port = loc.port || "";
    var host = (loc.hostname || "").toLowerCase();

    if (loc.protocol === "file:") {
        window.API_BASE = "http://localhost:" + DEFAULT_PORT;
        return;
    }

    if (
        port === DEFAULT_PORT &&
        (host === "localhost" || host === "127.0.0.1" || host === "::1")
    ) {
        window.API_BASE = loc.origin;
        return;
    }

    if (
        port === "5500" ||
        port === "5501" ||
        port === "5502" ||
        port === "8080" ||
        port === "3000" ||
        port === "5173" ||
        port === "4173"
    ) {
        window.API_BASE = "http://localhost:" + DEFAULT_PORT;
        return;
    }

    window.API_BASE = loc.origin;
})();

window.apiUrl = function (path) {
    if (!path) return window.API_BASE;
    if (path.charAt(0) !== "/") path = "/" + path;
    return window.API_BASE + path;
};
