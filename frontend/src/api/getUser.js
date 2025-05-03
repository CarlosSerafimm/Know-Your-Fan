import api from "@/api/api";



export const fetchUser = async () => {
  try {
    const res = await api.get("/fan/pesquisar");
    const data = res.data;

    return {
      dataNascimento: data.dataNascimento || null,
      estado: data.estado || "",
      eventosParticipados: data.eventosParticipados || [],
      genero: data.genero || "",
      instagram: data.instagram || "",
      jogadoresFavoritos: data.jogadoresFavoritos || [],
      jogosFavoritos: data.jogosFavoritos || [],
      linkedIn: data.linkedIn || "",
      login: data.login || "",
      nomeCompleto: data.nomeCompleto || "",
      pontuacao: data.pontuacao || 0,
      produtosComprados: data.produtosComprados || [],
      redesSeguidas: data.redesSeguidas || [],
      twitter: data.twitter || "",
      validado: data.validado ?? false,
      plataformasAssistidas: data.plataformasAssistidas || [],
    };
  } catch (err) {
    console.error("Erro ao buscar usuário:", err);
    return null;
  }
};
