
const API = (() => {
    if(window.location.hostname === 'localhost' || window.location.hostname === '127.000.1'){
        return 'http://localhost:8080';
    }
    else {
        return 'http://app:8080';
    }
})();

const form = document.getElementById('submitForm');
const descriptionLabel = document.getElementById('description');
descriptionLabel.innerText = "Lütfen bilgilerinizi eksiksiz ve doğru bir şekilde giriniz.";

form.addEventListener('submit', function(event) {
    event.preventDefault();

    const name = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;

    if(name === ""){
        descriptionLabel.innerText = "İsim alanı boş bırakılamaz.";
        descriptionLabel.style.color = "red";
        return;
    }
    if(email === ""){
        descriptionLabel.innerText = "E-Posta alanı boş bırakılamaz.";
        descriptionLabel.style.color = "red";
        return;
    }
    if(phone === ""){
        descriptionLabel.innerText = "Telefon alanı boş bırakılamaz.";
        descriptionLabel.style.color = "red";
        return;
    }
    const data = {
        name: name,
        email: email,
        phone: phone
    };

    fetch(API+'/api/registration', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(res => res.json())
    .then(response => {
        if(response.valid === false){
            descriptionLabel.innerText = response.message;
            descriptionLabel.style.color = "red";
            return;
        }
        else if(response.status === 500){
            descriptionLabel.innerText = "Email veya telefon zaten kayıtlı.";
            descriptionLabel.style.color = "red";
            return;
        }
        if(response.valid === true){
            descriptionLabel.innerText = response.data.name + ", kayıt işleminiz başarıyla tamamlandı!";
            descriptionLabel.style.color = "green";
            form.reset();
            return;
        }
    })
    .catch(error => {
        descriptionLabel.innerText = "Bir hata oluştu. Lütfen tekrar deneyiniz.";
        descriptionLabel.style.color = "red";
        console.error('Error:', error);
    });

});
    