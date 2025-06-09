"use client";

import { useState, useEffect } from "react";
import { Trash2 } from "lucide-react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Button } from "@/components/ui/button";

export default function SavedAdvicePage() {
  const [tips, setTips] = useState([]);
  const [loading, setLd] = useState(true);
  const [error, setErr] = useState("");

  useEffect(() => {
    fetch("/api/advice/history", { credentials: "include" })
      .then((r) => r.json())
      .then(setTips)
      .catch(() => setErr("Failed to load saved advice."))
      .finally(() => setLd(false));
  }, []);

  async function del(id) {
    if (!confirm("Delete this advice?")) return;
    const res = await fetch(`/api/advice/${id}`, {
      method: "DELETE",
      credentials: "include",
    });
    if (res.ok) setTips((t) => t.filter((x) => x.id !== id));
  }

  if (loading)
    return (
      <main className="min-h-screen flex items-center justify-center">
        Loading…
      </main>
    );
  if (error)
    return (
      <main className="min-h-screen flex items-center justify-center text-destructive">
        {error}
      </main>
    );

  return (
    <main className="p-6 space-y-6">
      <h1 className="text-2xl font-semibold">Saved Advice</h1>

      <Card>
        <CardHeader>
          <CardTitle>All your tips</CardTitle>
        </CardHeader>
        <CardContent className="p-0">
          <ScrollArea className="h-[420px]">
            <ul className="divide-y">
              {tips.map((t) => (
                <li key={t.id} className="flex items-start gap-2 p-4">
                  <p className="flex-1">{t.text}</p>
                  <Button
                    size="icon"
                    variant="ghost"
                    title="Delete"
                    onClick={() => del(t.id)}
                  >
                    <Trash2 className="size-4" />
                  </Button>
                </li>
              ))}
              {!tips.length && (
                <li className="p-4 text-muted-foreground">
                  No saved advice yet.
                </li>
              )}
            </ul>
          </ScrollArea>
        </CardContent>
      </Card>
    </main>
  );
}
