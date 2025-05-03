import { Card, CardContent } from "@/components/ui/card";
import UploadModal from "@/components/UploadModal";
import { Button } from "@/components/ui/button";
import { motion } from "framer-motion";
import furiaLogo from "@/assets/furia-logo.png";
import { useEffect, useState } from "react";
import {
  BadgeCheck,
  Gamepad2,
  User2,
  Calendar,
  Star,
  LinkIcon,
  MapPin,
  ShoppingBag,
  UserRound,
  Tv,
  Twitter,
  Instagram,
  Linkedin,
} from "lucide-react";
import { fetchUser } from "@/api/getUser";
import EditUserModal from "@/components/EditUserModal";
import api from "@/api/api";

const iconMap = {
  Login: <User2 className="w-4 h-4 text-fuchsia-500" />,
  "Nome Completo": <User2 className="w-4 h-4 text-fuchsia-500" />,
  Estado: <MapPin className="w-4 h-4 text-fuchsia-500" />,
  "Data de Nascimento": <Calendar className="w-4 h-4 text-fuchsia-500" />,
  Idade: <Calendar className="w-4 h-4 text-fuchsia-500" />,
  Gênero: <User2 className="w-4 h-4 text-fuchsia-500" />,
  "Jogos Favoritos": <Gamepad2 className="w-4 h-4 text-fuchsia-500" />,
  "Eventos Participados": <Star className="w-4 h-4 text-fuchsia-500" />,
  "Produtos Comprados": <ShoppingBag className="w-4 h-4 text-fuchsia-500" />,
  "Jogadores Favoritos": <UserRound className="w-4 h-4 text-fuchsia-500" />,
  "Plataformas Assistidas": <Tv className="w-4 h-4 text-fuchsia-500" />,
  "Redes Seguidas": <LinkIcon className="w-4 h-4 text-fuchsia-500" />,
  Twitter: <Twitter className="w-4 h-4 text-fuchsia-500" />,
  Instagram: <Instagram className="w-4 h-4 text-fuchsia-500" />,
  LinkedIn: <Linkedin className="w-4 h-4 text-fuchsia-500" />,
  Validado: <BadgeCheck className="w-4 h-4 text-fuchsia-500" />,
};

export const InfoItem = ({ label, value }) => (
  <div className="mb-4">
    <div className="flex items-center gap-2 mb-1">
      {iconMap[label] || null}
      <span className="text-sm uppercase text-gray-400 tracking-wider">
        {label}
      </span>
    </div>
    {Array.isArray(value) ? (
      <div className="flex flex-wrap gap-2">
        {value.map((item, i) => (
          <span
            key={i}
            className="bg-fuchsia-800/20 text-fuchsia-300 px-3 py-1 rounded-full text-xs font-medium backdrop-blur-sm border border-fuchsia-700/40"
          >
            {item}
          </span>
        ))}
      </div>
    ) : (
      <p className="text-white text-base font-medium">{value}</p>
    )}
  </div>
);

export default function User() {
  const [user, setUser] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [uploadModalOpen, setUploadModalOpen] = useState(false);
  const [uploadMessage, setUploadMessage] = useState("");
  const [uploadStatus, setUploadStatus] = useState(null);

  useEffect(() => {
    const loadUser = async () => {
      const fetchedUser = await fetchUser();
      setUser(fetchedUser);
    };
    loadUser();
  }, []);
  const handleImageUpload = async (file) => {
    window.scrollTo(0, 0);
    const formData = new FormData();
    formData.append("file", file);

    try {
      const res = await api.post("/fan/validar", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      setUploadMessage(res.data);
      setUploadStatus("success");
      setTimeout(() => {
        window.location.reload();
      }, 3000);
    } catch (err) {
      console.error("Erro ao enviar imagem:", err);
      const mensagem =
        err?.response?.data?.message ||
        "Erro ao enviar imagem. Tente novamente.";
      setUploadMessage(mensagem);
      setUploadStatus("error");
    }

    setTimeout(() => {
      setUploadMessage("");
      setUploadStatus(null);
    }, 5000);
  };
  const redirectToTwitchOAuth = () => {
    const clientId = "8lzpxyufeefqimduxk3grhqokp21tu";
    const redirectUri = "http://localhost:8080/oauth/twitch/callback"; // agora para o frontend!
    const token = localStorage.getItem("token");

    const authUrl = `https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=${clientId}&redirect_uri=${encodeURIComponent(
      redirectUri
    )}&scope=user:read:email&state=${encodeURIComponent(token)}`;

    window.location.href = authUrl;
  };

  const handleSave = async (cleanedData) => {
    try {
      const res = await api.put("/fan", cleanedData);
      setUser(res.data);
    } catch (error) {
      console.error("Erro ao salvar dados do usuário:", error);
    }
  };

  if (!user) {
    return <p className="text-white">Carregando...</p>;
  }
  const {
    login,
    nomeCompleto,
    estado,
    dataNascimento,
    genero,
    jogosFavoritos,
    eventosParticipados,
    produtosComprados,
    jogadoresFavoritos,
    plataformasAssistidas,
    redesSeguidas,
    twitter,
    instagram,
    linkedIn,
    validado,
    pontuacao,
  } = user;

  const calcularIdade = (dataNascimento) => {
    if (!dataNascimento) return null;
    const hoje = new Date();
    const nascimento = new Date(dataNascimento);
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const m = hoje.getMonth() - nascimento.getMonth();
    if (m < 0 || (m === 0 && hoje.getDate() < nascimento.getDate())) idade--;
    return idade;
  };

  const idade = calcularIdade(user.dataNascimento);
  return (
    <motion.div
      initial={{ opacity: 0, y: 30 }}
      animate={{ opacity: 1, y: 0 }}
      className="min-h-screen bg-gradient-to-b from-black via-zinc-950 to-zinc-900 text-white p-6 flex flex-col items-center"
    >
      <div className="w-full max-w-6xl">
        <div className="flex items-center justify-between mb-10">
          <h1 className="text-4xl font-extrabold tracking-tight text-fuchsia-500 drop-shadow">
            Perfil do Usuário - FURIA
          </h1>
          <div className="w-30 h-30 rounded-full p-2 ">
            <img
              src={furiaLogo}
              alt="furia-logo"
              className="w-full h-full object-contain"
            />
          </div>
        </div>

        {uploadMessage && (
          <div
            className={`mb-6 p-4 rounded-lg text-sm font-medium text-center transition-all duration-300 ${
              uploadStatus === "success"
                ? "bg-emerald-800 text-emerald-100 border border-emerald-500"
                : "bg-red-900 text-red-100 border border-red-500"
            }`}
          >
            {uploadMessage}
          </div>
        )}
        <Card className="bg-zinc-900/70 backdrop-blur-md border border-zinc-800 rounded-2xl shadow-2xl">
          <CardContent className="p-8 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <InfoItem label="Login" value={login} />
            <InfoItem label="Nome Completo" value={nomeCompleto} />
            <InfoItem label="Estado" value={estado} />
            <InfoItem label="Data de Nascimento" value={dataNascimento} />
            <InfoItem label="Idade" value={idade} />
            <InfoItem label="Gênero" value={genero} />
            <InfoItem label="Jogos Favoritos" value={jogosFavoritos} />
            <InfoItem
              label="Eventos Participados"
              value={eventosParticipados}
            />
            <InfoItem label="Produtos Comprados" value={produtosComprados} />
            <InfoItem label="Jogadores Favoritos" value={jogadoresFavoritos} />
            <InfoItem
              label="Plataformas Assistidas"
              value={plataformasAssistidas}
            />
            <InfoItem label="Redes Seguidas" value={redesSeguidas} />
            <InfoItem label="Twitter" value={twitter} />
            <InfoItem label="Instagram" value={instagram} />
            <InfoItem label="LinkedIn" value={linkedIn} />
            <InfoItem label="Validado" value={validado ? "Sim ✅" : "Não ❌"} />

            <div className="col-span-full mt-6">
              <p className="text-lg font-semibold text-fuchsia-400 mb-2">
                Nível de fã
              </p>
              <div className="w-full h-6 bg-zinc-800 rounded-full overflow-hidden">
                <div
                  className="bg-gradient-to-r from-fuchsia-500 to-purple-700 h-full rounded-full transition-all duration-500"
                  style={{ width: `${pontuacao}%` }}
                />
              </div>
              <p className="mt-1 text-sm text-white">{pontuacao}%</p>
            </div>
          </CardContent>
        </Card>

        <div className="mt-8 flex justify-end gap-3">
          <Button
            onClick={() => setUploadModalOpen(true)}
            className="cursor-pointer bg-fuchsia-600 hover:bg-fuchsia-700 text-white font-semibold px-6 py-2 rounded-xl shadow-md hover:shadow-lg transition-all"
          >
            Validar
          </Button>
          <Button
            onClick={() => setModalOpen(true)}
            className="cursor-pointer bg-fuchsia-600 hover:bg-fuchsia-700 text-white font-semibold px-6 py-2 rounded-xl shadow-md hover:shadow-lg transition-all"
          >
            Editar Informações
          </Button>
          <Button
            onClick={redirectToTwitchOAuth}
            className="cursor-pointer bg-fuchsia-600 hover:bg-fuchsia-700 text-white font-semibold px-6 py-2 rounded-xl shadow-md hover:shadow-lg transition-all"
          >
            twitch
          </Button>
        </div>
      </div>
      <UploadModal
        open={uploadModalOpen}
        onClose={() => setUploadModalOpen(false)}
        onUpload={handleImageUpload}
      />
      <EditUserModal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        userData={user}
        onSave={handleSave}
      />
    </motion.div>
  );
}
