"use client";

import Link from "next/link";
import { useAuth } from "@/hooks/use-auth";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardFooter,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";

export default function Home() {
  const { authed, loading } = useAuth();

  const cards = [
    {
      title: "Your Advice",
      desc: "AI-powered money-saving tips",
      href: "/advice",
    },
    {
      title: "Saved Advice",
      desc: "All tips you’ve kept",
      href: "/saved-advice",
    },
    { title: "Dashboard", desc: "Visualise your spending", href: "/dashboard" },
    { title: "Transactions", desc: "Your full history", href: "/transactions" },
    { title: "Savings Goals", desc: "Track your goals", href: "/goals" },
  ];

  return (
    <main className="min-h-screen p-6">
      <h1 className="text-3xl font-bold mb-6 text-center">
        Welcome to Pennywise
      </h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {cards.map((c) => (
          <Card key={c.href} className="hover:shadow-lg transition-shadow">
            <CardHeader>
              <CardTitle>{c.title}</CardTitle>
              <CardDescription>{c.desc}</CardDescription>
            </CardHeader>
            <CardFooter>
              <Link href={authed ? c.href : "/login"} prefetch={false} passHref>
                <Button disabled={!authed && !loading} asChild>
                  <a>Go</a>
                </Button>
              </Link>
            </CardFooter>
          </Card>
        ))}
      </div>
    </main>
  );
}
