import { useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { motion } from "framer-motion";
import furiaLogo from "@/assets/furia-logo.png";
import { UserPlus, Eye, EyeOff } from "lucide-react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function Register({ toggle }) {
  const [login, setLogin] = useState("");
  const [senha, setSenha] = useState("");
  const [confirmaSenha, setConfirmaSenha] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [erro, setErro] = useState("");
  const navigate = useNavigate();

  const senhasIguais =
    Boolean(senha) && Boolean(confirmaSenha) && senha === confirmaSenha;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!senhasIguais) return;

    try {
      await axios.post("http://localhost:8080/auth/register", { login, senha });
      const response = await axios.post("http://localhost:8080/auth/login", {
        login,
        senha,
      });
      const { token } = response.data;
      localStorage.setItem("token", token);
      navigate("/user");
    } catch (error) {
      console.error("Erro no registro/login:", error);
      setErro("Erro ao criar conta. Verifique os dados ou tente novamente.");
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 40 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className="bg-zinc-800 rounded-2xl shadow-lg p-8 w-full max-w-sm flex flex-col items-center"
    >
      <div className="flex items-center gap-2 bg-green-800/40 text-green-300 text-xs px-3 py-1 rounded-full mb-5 uppercase tracking-widest">
        <UserPlus className="w-4 h-4" />
        <span>Registro</span>
      </div>

      <div className="w-20 h-20 rounded-full mb-6">
        <img src={furiaLogo} alt="furia-logo" />
      </div>

      <h1 className="text-2xl font-bold mb-4 text-center">
        Junte-se à Alcateia!
      </h1>

      <Input
        className="mb-3"
        placeholder="Usuário"
        value={login}
        onChange={(e) => setLogin(e.target.value)}
      />

      <div className="relative mb-4 w-full">
        <Input
          type={showPassword ? "text" : "password"}
          placeholder="Senha"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
        />
        <button
          type="button"
          onClick={() => setShowPassword((prev) => !prev)}
          className="absolute right-2 top-1/2 -translate-y-1/2 text-zinc-400 hover:text-white"
        >
          {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
        </button>
      </div>

      <div className="relative mb-4 w-full">
        <Input
          type={showPassword ? "text" : "password"}
          placeholder="Repita a sua senha"
          value={confirmaSenha}
          onChange={(e) => setConfirmaSenha(e.target.value)}
        />
        <button
          type="button"
          onClick={() => setShowPassword((prev) => !prev)}
          className="absolute right-2 top-1/2 -translate-y-1/2 text-zinc-400 hover:text-white"
        >
          {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
        </button>
      </div>

      {senha && confirmaSenha && senha !== confirmaSenha && (
        <p className="text-red-500 text-xs mb-2">As senhas não coincidem</p>
      )}

      <Button
        onClick={handleSubmit}
        disabled={!senhasIguais}
        className="w-full bg-blue-600 hover:bg-blue-700 disabled:opacity-50 disabled:hover:bg-blue-600 disabled:cursor-not-allowed"
      >
        Register
      </Button>

      <p className="mt-4 text-sm text-center">
        Já faz parte da matilha?{" "}
        <button
          onClick={toggle}
          className="text-sm text-blue-600 hover:text-blue-800 underline transition cursor-pointer"
        >
          Login
        </button>
      </p>
    </motion.div>
  );
}
