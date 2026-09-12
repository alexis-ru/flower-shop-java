function renderHeader() {
    const html = `
    <nav class="navbar navbar-expand-lg navbar-dark bg-success">
        <div class="container">
            <a class="navbar-brand" href="/pages/index.html">&#127804; Василёк</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#nav-menu">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="nav-menu">
                <ul class="navbar-nav ms-auto" id="nav-items"></ul>
            </div>
        </div>
    </nav>`;
    document.getElementById('header-placeholder').innerHTML = html;
    updateNavMenu();
}

function updateNavMenu() {
    const navItems = document.getElementById('nav-items');
    if (!navItems) return;

    fetch('/api/auth/current')
        .then(r => r.ok ? r.json() : null)
        .then(user => {
            if (!user) {
                navItems.innerHTML = '';
                return;
            }
            let links = '';
            if (user.role === 'DIRECTOR') {
                links += '<li class="nav-item"><a class="nav-link" href="/pages/director.html">Кабинет директора</a></li>';
            } else {
                links += '<li class="nav-item"><a class="nav-link" href="/pages/seller.html">Кабинет продавца</a></li>';
            }
            links += `<li class="nav-item"><span class="nav-link text-light">${user.fullName}</span></li>`;
            links += '<li class="nav-item"><a class="nav-link" href="#" id="logout-link">Выйти</a></li>';
            navItems.innerHTML = links;
            document.getElementById('logout-link')?.addEventListener('click', e => {
                e.preventDefault();
                fetch('/api/auth/logout', { method: 'POST' })
                    .finally(() => window.location.href = '/pages/index.html');
            });
        })
        .catch(() => navItems.innerHTML = '');
}

function renderFooter() {
    const html = `
    <footer class="footer mt-auto py-3 bg-light border-top">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6" id="weather-section">
                    <small class="text-muted">Загрузка погоды…</small>
                </div>
                <div class="col-md-6 text-md-end" id="currency-section">
                    <small class="text-muted">Загрузка курсов валют…</small>
                </div>
            </div>
        </div>
    </footer>`;
    document.getElementById('footer-placeholder').innerHTML = html;
    loadWeather();
    loadCurrency();
}

function loadWeather() {
    // Москва по умолчанию; координаты можно поменять на текущий регион
    const lat = 55.7558, lon = 37.6173;
    const url = `https://api.open-meteo.com/v1/forecast?latitude=${lat}&longitude=${lon}` +
                `&current=temperature_2m,weather_code,wind_speed_10m&timezone=Europe/Moscow`;

    fetch(url)
        .then(r => r.json())
        .then(data => {
            const t = Math.round(data.current.temperature_2m);
            const wind = Math.round(data.current.wind_speed_10m);
            const code = data.current.weather_code;
            const desc = weatherDesc(code);
            document.getElementById('weather-section').innerHTML =
                `<small><strong>Москва</strong> (gismeteo.ru): ${desc}, ${t}°C, ветер ${wind} м/с</small>`;
        })
        .catch(() => {
            document.getElementById('weather-section').innerHTML =
                `<small class="text-muted">Погода недоступна — <a href="https://www.gismeteo.ru/weather/moscow/4368/" target="_blank">gismeteo.ru</a></small>`;
        });
}

function weatherDesc(code) {
    const map = {
        0: 'Ясно', 1: 'Преимущественно ясно', 2: 'Переменная облачность',
        3: 'Пасмурно', 45: 'Туман', 48: 'Изморозь',
        51: 'Морось', 53: 'Морось', 55: 'Морось',
        61: 'Небольшой дождь', 63: 'Дождь', 65: 'Сильный дождь',
        71: 'Небольшой снег', 73: 'Снег', 75: 'Сильный снег',
        80: 'Ливень', 81: 'Ливень', 82: 'Сильный ливень',
        95: 'Гроза', 96: 'Гроза с градом', 99: 'Сильная гроза'
    };
    return map[code] || '—';
}

// --- Курсы валют (ЦБ РФ) ---
function loadCurrency() {
    fetch('https://www.cbr-xml-daily.ru/daily_json.js')
        .then(r => r.json())
        .then(data => {
            const valute = data.Valute;
            const usd = valute.USD.Value.toFixed(2);
            const eur = valute.EUR.Value.toFixed(2);
            const cny = valute.CNY.Value.toFixed(2);
            document.getElementById('currency-section').innerHTML =
                `<small><strong>ЦБ РФ:</strong> USD ${usd}₽ · EUR ${eur}₽ · CNY ${cny}₽</small>`;
        })
        .catch(() => {
            document.getElementById('currency-section').innerHTML =
                `<small class="text-muted">Курсы валют недоступны</small>`;
        });
}

document.addEventListener('DOMContentLoaded', () => {
    renderHeader();
    renderFooter();
});
