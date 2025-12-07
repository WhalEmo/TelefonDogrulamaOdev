const userList = document.getElementById('userList');

const API = (() => {
    if (window.location.hostname === 'localhost' || window.location.hostname === '127.000.1') {
        return 'http://localhost:8080';
    }
    else {
        return 'http://app:8080';
    }
})();

fetch(API + '/api/users?page=0&size=5', {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
    }
})
.then(res => res.json())
.then(response => {
    if(Array.isArray(response)){
        createUserList(response);
        return;
    }
    else if(response.content){
        createUserList(response.content);
        return;
    }
    else {
        userList.innerText = "Kullanıcı verileri alınamadı.";
        return;
    }
})
.catch(error => {
    console.error('Error:', error);
    userList.innerText = "API hatası.";
});

function createUserList(users){
    const userList = document.getElementById('userList');
    userList.innerHTML = '';

    users.forEach(u => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${u.id}</td>
            <td>${u.name}</td>
            <td>${u.email}</td>
            <td>${u.phone}</td>
            <td>${new Date(u.createdAt).toLocaleString('tr-TR')}</td>
        `;

        userList.appendChild(row);
    });
}