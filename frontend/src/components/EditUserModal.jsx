import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useEffect, useState } from "react";
import axios from "axios";
import api from "@/api/api";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";

export default function EditUserModal({ open, onClose, userData, onSave }) {
  const [formData, setFormData] = useState(userData || {});
  const [enumss, setEnumss] = useState({});

  useEffect(() => {
    if (open) {
      const fetchEnums = async () => {
        try {
          const res = await api.get("/enums");
          const rawEnums = res.data;

          const mapped = {
            jogosFavoritos: rawEnums["Jogo"] || [],
            eventosParticipados: rawEnums["Evento"] || [],
            produtosComprados: rawEnums["Produto"] || [],
            jogadoresFavoritos: rawEnums["Jogador"] || [],
            plataformasAssistidas: rawEnums["Plataforma"] || [],
            redesSeguidas: rawEnums["RedeSocial"] || [],
            estado: rawEnums["Estado"] || [],
            genero: rawEnums["Genero"] || [],
          };

          setEnumss(mapped);
        } catch (err) {
          console.error("Erro ao buscar enums:", err);
        }
      };

      fetchEnums();
    }
  }, [open]);

  useEffect(() => {
    setFormData(userData || {});
  }, [userData]);

  const handleCheckboxChange = (field, value) => {
    const current = formData[field] || [];
    if (current.includes(value)) {
      setFormData({ ...formData, [field]: current.filter((v) => v !== value) });
    } else {
      setFormData({ ...formData, [field]: [...current, value] });
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSave = () => {
    const { validado, login, pontuacao, idade, ...rawData } = formData;

    const cleanedData = Object.entries(rawData).reduce((acc, [key, value]) => {
      if (
        value !== "" && 
        value !== undefined && 
        value !== null &&
        !(Array.isArray(value) && value.length === 0) 
      ) {
        acc[key] = value;
      }
      return acc;
    }, {});

    onSave(cleanedData);
    onClose();
  };

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl bg-zinc-950 border border-purple-950 shadow-xl rounded-xl text-zinc-200">
        <DialogHeader>
          <DialogTitle className="text-2xl font-semibold text-purple-300">
            Editar Informações
          </DialogTitle>
        </DialogHeader>
        <ScrollArea className="h-[70vh] pr-4 scrollbar-thin scrollbar-thumb-purple-600/40 scrollbar-track-transparent">
          <div className="space-y-5 px-1 py-2">
            <div>
              <Label className="text-zinc-300">Nome Completo</Label>
              <Input
                name="nomeCompleto"
                value={formData.nomeCompleto || ""}
                onChange={handleChange}
                className="bg-zinc-900 border border-zinc-700 text-zinc-100 placeholder-zinc-400 focus:ring-purple-500"
              />
            </div>

            {/* Estado */}
            <div>
              <Label className="text-zinc-300">Estado</Label>
              <Select
                value={formData.estado || ""}
                onValueChange={(value) =>
                  setFormData({ ...formData, estado: value })
                }
              >
                <SelectTrigger className="bg-zinc-900 border border-zinc-700 text-zinc-100 focus:ring-purple-500">
                  <SelectValue placeholder="Selecione um estado" />
                </SelectTrigger>
                <SelectContent className="bg-zinc-900 text-zinc-200">
                  {enumss.estado?.map((item) => (
                    <SelectItem key={item} value={item}>
                      {item}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {[
              {
                label: "Data de Nascimento",
                name: "dataNascimento",
                type: "date",
              },
              { label: "Twitter", name: "twitter" },
              { label: "Instagram", name: "instagram" },
              { label: "LinkedIn", name: "linkedIn" },
            ].map(({ label, name, type }) => (
              <div key={name}>
                <Label className="text-zinc-300">{label}</Label>
                <Input
                  type={type || "text"}
                  name={name}
                  value={formData[name] || ""}
                  onChange={handleChange}
                  className="bg-zinc-900 border border-zinc-700 text-zinc-100 placeholder-zinc-400 focus:ring-purple-500"
                />
              </div>
            ))}

            <div>
              <Label className="text-zinc-300">Gênero</Label>
              <Select
                value={formData.genero || ""}
                onValueChange={(value) =>
                  setFormData({ ...formData, genero: value })
                }
              >
                <SelectTrigger className="bg-zinc-900 border border-zinc-700 text-zinc-100 focus:ring-purple-500">
                  <SelectValue placeholder="Selecione um gênero" />
                </SelectTrigger>
                <SelectContent className="bg-zinc-900 text-zinc-200">
                  {enumss.genero?.map((item) => (
                    <SelectItem key={item} value={item}>
                      {item}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {[
              "jogosFavoritos",
              "eventosParticipados",
              "produtosComprados",
              "jogadoresFavoritos",
              "plataformasAssistidas",
              "redesSeguidas",
            ].map((key) =>
              enumss[key]?.length ? (
                <div key={key}>
                  <Label className="text-zinc-300 capitalize">{key}</Label>
                  <div className="flex flex-wrap gap-3 mt-2">
                    {enumss[key].map((option) => (
                      <div
                        key={option}
                        className="flex items-center gap-2 bg-zinc-900 px-2 py-1 rounded-md border border-zinc-700"
                      >
                        <Checkbox
                          id={`${key}-${option}`}
                          checked={formData[key]?.includes(option)}
                          onCheckedChange={() =>
                            handleCheckboxChange(key, option)
                          }
                          className="border-zinc-600 bg-zinc-800"
                        />
                        <Label
                          htmlFor={`${key}-${option}`}
                          className="text-zinc-200 text-sm"
                        >
                          {option}
                        </Label>
                      </div>
                    ))}
                  </div>
                </div>
              ) : null
            )}
          </div>
        </ScrollArea>

        <div className="flex justify-end gap-4 mt-6">
          <Button
            variant="outline"
            onClick={onClose}
            className="cursor-pointer bg-zinc-950 border-zinc-600 text-zinc-300 hover:bg-zinc-800 hover:text-white"
          >
            Cancelar
          </Button>
          <Button
            onClick={handleSave}
            className="cursor-pointer bg-purple-600 text-white hover:bg-purple-500"
          >
            Salvar
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}
