document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/auth/current')
        .then(r => r.ok ? r.json() : null)
        .then(user => {
            if (user) {
                window.location.href = user.role === 'DIRECTOR'
                    ? '/pages/director.html' : '/pages/seller.html';
            }
        });

    const form = document.getElementById('login-form');
    const errorMsg = document.getElementById('error-msg');

    form.addEventListener('submit', e => {
        e.preventDefault();
        errorMsg.classList.add('d-none');

        const login = document.getElementById('login').value.trim();
        const password = document.getElementById('password').value;

        fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ login, password })
        })
        .then(r => r.json())
        .then(data => {
            if (data.error) {
                errorMsg.textContent = data.error;
                errorMsg.classList.remove('d-none');
                return;
            }
            window.location.href = data.role === 'DIRECTOR'
                ? '/pages/director.html' : '/pages/seller.html';
        })
        .catch(() => {
            errorMsg.textContent = 'Ошибка соединения с сервером';
            errorMsg.classList.remove('d-none');
        });
    });
});
