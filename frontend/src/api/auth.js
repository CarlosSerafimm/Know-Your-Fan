
export function isTokenValid() {
    const token = localStorage.getItem("token");
    if (!token) {
      return false;
    }
  
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiration = payload.exp * 1000;
      return Date.now() < expiration;
    } catch (error) {
      console.error("Erro ao validar token:", error);
      return false;
    }
  }
  