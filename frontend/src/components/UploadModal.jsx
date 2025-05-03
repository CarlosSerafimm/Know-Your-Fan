import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useState } from "react";

export default function UploadModal({ open, onClose, onUpload }) {
  const [selectedFile, setSelectedFile] = useState(null);

  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
  };

  const handleUpload = () => {
    if (!selectedFile) return;
    onUpload(selectedFile);
    onClose();
  };

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="bg-zinc-950 border border-purple-800 text-white max-w-md">
        <DialogHeader>
          <DialogTitle className="text-purple-300">Enviar Imagem</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 mt-4">
          <div className="relative w-full">
            <label
              htmlFor="fileUpload"
              className="flex items-center justify-center px-4 py-2 bg-zinc-800 border-2 border-dashed border-purple-600 rounded-lg cursor-pointer hover:bg-zinc-700 transition-colors text-sm text-purple-300"
            >
              {selectedFile
                ? selectedFile.name
                : "Envie uma foto do seu documento"}
            </label>
            <Input
              id="fileUpload"
              type="file"
              accept="image/*"
              onChange={handleFileChange}
              className="hidden"
            />
          </div>

          <Button
            onClick={handleUpload}
            disabled={!selectedFile}
            className="bg-fuchsia-600 hover:bg-fuchsia-700 text-white w-full"
          >
            Enviar
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}
