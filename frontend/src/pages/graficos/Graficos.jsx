import { useEffect, useState } from "react";
import api from "@/api/api";
import { Card, CardContent } from "@/components/ui/card";
import { Loader2 } from "lucide-react";
import { Chart } from "react-google-charts";
import { motion } from "framer-motion";

export default function Graficos() {
  const [dados, setDados] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await api.get("/api/graficos/dadosAgregados");
        setDados(res.data);
      } catch (err) {
        console.error("Erro ao buscar dados:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const gerarDadosGrafico = (titulo, objeto) => {
    const dados = [[titulo, "Quantidade"]];
    for (const chave in objeto) {
      const valor = objeto[chave];
      const label = `${chave} (${valor})`;
      dados.push([label, valor === 0 ? 0.000001 : valor]);
    }
    return dados;
  };

  const renderGrafico = (titulo, objeto) => {
    const isBarChart = titulo === "Estado" || titulo === "Produtos Comprados";
    const chartType = isBarChart ? "ColumnChart" : "PieChart";

    const chartOptions = {
      backgroundColor: "transparent",
      legend: { textStyle: { color: "#f1f1f1", fontSize: 12 } },
      ...(isBarChart && {
        hAxis: { textStyle: { color: "#ccc" } },
        vAxis: { textStyle: { color: "#ccc" }, minValue: 0 },
        chartArea: { width: "80%", height: "70%" },
      }),
    };

    return (
      <motion.div
        whileHover={{ scale: 1.05 }}
        transition={{ duration: 0.3 }}
        className="w-full sm:w-[48%] lg:w-[48%] mb-6" // Ajustado para 2 por linha
      >
        <Card className="bg-gradient-to-br from-zinc-800 to-zinc-900 border border-zinc-700 shadow-xl rounded-3xl p-4 transition-transform duration-300">
          <h2 className="text-lg font-bold text-purple-400 mb-3 tracking-wide">
            {titulo}
          </h2>
          <Chart
            chartType={chartType}
            width="100%"
            height="240px"
            data={gerarDadosGrafico(titulo, objeto)}
            options={chartOptions}
          />
        </Card>
      </motion.div>
    );
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-zinc-950">
        <Loader2 className="h-10 w-10 animate-spin text-purple-500" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-zinc-950 text-white px-6 py-8">
      <motion.h1
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="text-4xl font-extrabold text-purple-500 mb-10 text-center"
      >
        Painel de Gráficos
      </motion.h1>
      <div className="flex flex-wrap justify-center gap-6">
        {renderGrafico("Gênero", dados.generoCount)}
        {renderGrafico("Estado", dados.estadoCount)}
        {renderGrafico("Jogos Favoritos", dados.jogoCount)}
        {renderGrafico("Eventos", dados.eventoCount)}
        {renderGrafico("Produtos Comprados", dados.produtoCount)}
        {renderGrafico("Plataformas Assistidas", dados.plataformaCount)}
        {renderGrafico("Redes Sociais", dados.redeSocialCount)}
        {renderGrafico("Jogadores Favoritos", dados.jogadorCount)}
        {renderGrafico("Validado", {
          Validado: dados.validadoTrueCount,
          "Não Validado": dados.validadoFalseCount,
        })}
        {renderGrafico("Segue a FURIA", {
          Sim: dados.segueFuriaTrueCount,
          Não: dados.segueFuriaFalseCount,
        })}
      </div>
    </div>
  );
}
