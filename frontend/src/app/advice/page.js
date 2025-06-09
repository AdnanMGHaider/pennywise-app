"use client";

import { useState, useEffect, useCallback } from "react";
import { X, Save } from "lucide-react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

/* ――― tiny toast util ――― */
function Toast({ message, variant = "default", onClose }) {
  const bg = variant === "error" ? "bg-destructive" : "bg-secondary/20";
  const txt = variant === "error" ? "text-white" : "text-foreground";
  return (
    <div
      className={`${bg} ${txt} fixed bottom-4 right-4 flex items-center space-x-2 px-4 py-2 rounded-lg shadow-lg z-50`}
    >
      <span>{message}</span>
      <button onClick={onClose} className="focus:outline-none">
        <X size={16} />
      </button>
    </div>
  );
}

export default function AdvicePage() {
  const [adviceText, setAdviceText] = useState("");
  const [loading, setLoading] = useState(true);
  const [toasts, setToasts] = useState([]);
  const [savedIds, setSavedIds] = useState(new Set()); // ids returned by POST /api/advice

  /* ——— toast helpers ——— */
  const removeToast = useCallback(
    (id) => setToasts((t) => t.filter((x) => x.id !== id)),
    []
  );

  const addToast = useCallback(
    (msg, v = "default") => {
      const id = Date.now();
      setToasts((t) => [...t, { id, msg, v }]);
      setTimeout(() => removeToast(id), 4000);
      return id;
    },
    [removeToast]
  );

  /* ——— initial fetch ― advice text ―—— */
  useEffect(() => {
    const id = addToast("Loading advice…");
    fetch("/api/advice", { credentials: "include" })
      .then((r) => r.json())
      .then((j) => setAdviceText(j.text ?? j.advice ?? "")) // compat
      .catch(() => addToast("Couldn’t load advice", "error"))
      .finally(() => {
        setLoading(false);
        removeToast(id);
      });
  }, [addToast]);

  /* ——— save one line ——— */
  async function handleSave(line) {
    try {
      const res = await fetch("/api/advice", {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ text: line }),
      });
      if (!res.ok) throw new Error();
      const { id } = await res.json();
      setSavedIds(new Set(savedIds).add(id));
      addToast("Saved!", "default");
    } catch {
      addToast("Couldn’t save – try again", "error");
    }
  }

  /* ――― split chat bubbles ――― */
  const messages = adviceText
    .split("\n")
    .map((l) => l.trim())
    .filter(Boolean);

  return (
    <main className="min-h-screen p-6 flex flex-col">
      <Card className="flex-1 flex flex-col">
        <CardHeader>
          <CardTitle>Your AI-Powered Advice</CardTitle>
        </CardHeader>
        <CardContent className="flex-1 overflow-y-auto space-y-4">
          {loading
            ? "Loading…"
            : messages.map((msg, i) => (
                <div key={i} className="flex items-start gap-2">
                  <div className="bg-secondary/20 p-4 rounded-2xl max-w-lg">
                    {msg}
                  </div>
                  <Button
                    variant="ghost"
                    size="icon"
                    title="Save"
                    onClick={() => handleSave(msg)}
                  >
                    <Save className="size-4" />
                  </Button>
                </div>
              ))}
        </CardContent>
      </Card>

      {toasts.map((t) => (
        <Toast
          key={t.id}
          message={t.msg}
          variant={t.v}
          onClose={() => removeToast(t.id)}
        />
      ))}
    </main>
  );
}
