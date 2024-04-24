async function login(){
    let datos = {};
    datos.user = document.getElementById('txtUser').value;
    datos.password = document.getElementById('txtPassword').value;

  const request = await fetch('http://localhost:8080/auth/login', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(datos)
  });

  const respuesta = await request.text();
  console.log(respuesta);
  if (respuesta != 'FAIL') {
    localStorage.token = respuesta;
    localStorage.user = datos.user;
    window.location.href = 'home.html'
  } else {
    alert("Las credenciales son incorrectas. Por favor intente nuevamente.");
  }

};