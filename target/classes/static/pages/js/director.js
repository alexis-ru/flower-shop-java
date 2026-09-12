document.addEventListener('DOMContentLoaded', () => {
      fetch('/api/auth/current')
        .then(r => r.ok ? r.json() : null)
        .then(user => {
            if (!user || user.role !== 'DIRECTOR') {
                window.location.href = '/pages/index.html';
            }
        });

    loadUsers();

    document.getElementById('add-user-form').addEventListener('submit', e => {
        e.preventDefault();
        const fullName = document.getElementById('new-full-name').value.trim();
        const login = document.getElementById('new-login').value.trim();
        const password = document.getElementById('new-password').value;

        fetch('/api/director/users', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ fullName, login, password, role: 'SELLER' })
        })
        .then(r => {
            if (r.ok) { e.target.reset(); loadUsers(); }
            else return r.json().then(d => alert(d.error || 'Ошибка'));
        });
    });
});

function loadUsers() {
    fetch('/api/director/users')
        .then(r => r.json())
        .then(users => {
            const tbody = document.getElementById('users-tbody');
            tbody.innerHTML = users.map(u => {
                const statusClass = `status-${u.status.toLowerCase()}`;
                const statusText = { WORKING: 'Работает', FIRED: 'Уволен', BLOCKED: 'Заблокирован' }[u.status];
                const blockDate = u.blockDate || '—';
                const dismissalDate = u.dismissalDate || '—';

                let actions = '';
                if (u.status === 'WORKING') {
                    actions += `<button class="btn btn-sm btn-warning me-1" onclick="blockUser(${u.id})">Заблокировать</button>`;
                } else if (u.status === 'BLOCKED') {
                    actions += `<button class="btn btn-sm btn-success me-1" onclick="unblockUser(${u.id})">Разблокировать</button>`;
                }
                if (u.status !== 'FIRED') {
                    actions += `<button class="btn btn-sm btn-danger" onclick="fireUser(${u.id})">Уволить</button>`;
                }
                return `<tr>
                    <td>${u.fullName}</td>
                    <td>${u.login}</td>
                    <td>${u.role === 'DIRECTOR' ? 'Директор' : 'Продавец'}</td>
                    <td class="${statusClass}">${statusText}</td>
                    <td>${u.registrationDate || '—'}</td>
                    <td>${blockDate}</td>
                    <td>${dismissalDate}</td>
                    <td>${actions}</td>
                </tr>`;
            }).join('');
        });
}

function blockUser(id) {
    fetch(`/api/director/users/${id}/block`, { method: 'PUT' })
        .then(() => loadUsers());
}

function unblockUser(id) {
    fetch(`/api/director/users/${id}/unblock`, { method: 'PUT' })
        .then(() => loadUsers());
}

function fireUser(id) {
    if (!confirm('Уволить сотрудника?')) return;
    fetch(`/api/director/users/${id}`, { method: 'DELETE' })
        .then(r => r.ok ? loadUsers() : r.json().then(d => alert(d.error || 'Ошибка')));
}
