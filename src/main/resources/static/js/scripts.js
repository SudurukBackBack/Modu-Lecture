// refresh token
window.fetchWithAuth = async function (url, options = {}) {
    let response = await fetch(url, options);

    if (response.status === 401) {
        const refreshRes = await fetch(
            "/api/auth/refresh",
            {
                method: "POST",
                credentials: "include"
            }
        );

        if (refreshRes.ok) {
            return await fetch(url, options); // 재시도
        } else {
            window.location.href = "/web/auth/login";
        }
    }

    return response;
};