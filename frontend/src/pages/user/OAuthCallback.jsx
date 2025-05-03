// src/pages/OAuthCallback.jsx (ou similar)
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const OAuthCallback = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const code = params.get("code");
    const token = params.get("state");

    if (!code || !token) {
      alert("Erro ao autenticar com a Twitch.");
      return;
    }

    fetch(`http://localhost:8080/oauth/twitch/callback?code=${code}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro na vinculação");
        return res.text();
      })
      .then(() => {
        alert("Conta Twitch vinculada com sucesso!");
        navigate("http://localhost:5173/user");
      })
      .catch((err) => {
        console.error(err);
        alert("Erro ao vincular conta Twitch.");
      });
  }, [navigate]);

  return <p>Vinculando sua conta Twitch...</p>;
};

export default OAuthCallback;
