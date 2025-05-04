import api from "./api";

function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      window
        .atob(base64)
        .split('')
        .map(function (c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join('')
    );

    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error("Erro ao decodificar o token", error);
    return null;
  }
}

export async function decodeJwt() {
  const token = localStorage.getItem("token");
  if (!token) {
    throw new Error("Token não encontrado");
  }

  const decodedToken = parseJwt(token);
  if (!decodedToken || !decodedToken.sub) {
    throw new Error("Token inválido ou sem 'sub'");
  }

  const username = decodedToken.sub;

  return username;
}
