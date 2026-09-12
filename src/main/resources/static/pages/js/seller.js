document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/auth/current')
        .then(r => r.ok ? r.json() : null)
        .then(user => {
            if (!user || user.role !== 'SELLER') {
                window.location.href = '/pages/index.html';
            }
        });

    document.getElementById('flower-arrival').value = new Date().toISOString().split('T')[0];

    loadFlowers();

    document.getElementById('add-flower-form').addEventListener('submit', e => {
        e.preventDefault();
        const name = document.getElementById('flower-name').value.trim();
        const quantity = parseInt(document.getElementById('flower-qty').value);
        const arrivalDate = document.getElementById('flower-arrival').value;

        fetch('/api/seller/flowers', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, quantity, arrivalDate })
        })
        .then(r => {
            if (r.ok) { e.target.reset(); loadFlowers(); }
            else alert('Ошибка при добавлении');
        });
    });
});

function loadFlowers() {
    fetch('/api/seller/flowers')
        .then(r => r.json())
        .then(flowers => {
            const tbody = document.getElementById('flowers-tbody');
            tbody.innerHTML = flowers.map(f => `
                <tr>
                    <td>${f.name}</td>
                    <td>${f.quantity}</td>
                    <td>${f.arrivalDate || '—'}</td>
                    <td>${f.saleDate || '—'}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-success" onclick="sellFlower(${f.id})">
                            Продать
                        </button>
                    </td>
                </tr>
            `).join('');
        });
}

function sellFlower(id) {
    const saleDate = new Date().toISOString().split('T')[0];
    fetch(`/api/seller/flowers/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ saleDate })
    })
    .then(() => loadFlowers());
}
