"use client";

import { useState, useEffect, useMemo } from "react";
import {
  PieChart,
  Pie,
  Cell,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  ResponsiveContainer,
} from "recharts";

import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import {
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";

/* cycle through your CSS palette */
const palette = (i) => `var(--chart-${(i % 5) + 1})`;

/* tiny util for currency */
const fmt = (n) =>
  `$${Number(n).toLocaleString(undefined, { maximumFractionDigits: 0 })}`;

export default function DashboardPage() {
  const [categories, setCategories] = useState([]);
  const [daily, setDaily] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  /* fetch data once */
  useEffect(() => {
    (async () => {
      try {
        const [cRes, dRes] = await Promise.all([
          fetch("/api/charts/categories", { credentials: "include" }),
          fetch("/api/charts/daily", { credentials: "include" }),
        ]);
        if (!cRes.ok || !dRes.ok) throw new Error("Dashboard fetch failed");

        setCategories(await cRes.json());
        setDaily(await dRes.json());
      } catch (err) {
        console.error(err);
        setError("Failed to load dashboard data.");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  /* config for ChartContainer (legend colours, tooltip, etc.) */
  const catConfig = useMemo(() => {
    const obj = {};
    categories.forEach((c, i) => {
      obj[c.category] = { label: c.category, color: palette(i) };
    });
    return obj;
  }, [categories]);

  /* ------------- status screens ------------- */
  if (loading)
    return (
      <main className="min-h-screen flex items-center justify-center">
        Loading dashboard…
      </main>
    );
  if (error)
    return (
      <main className="min-h-screen flex items-center justify-center">
        <p className="text-destructive">{error}</p>
      </main>
    );

  /* ------------- UI ------------- */
  return (
    <main className="p-6 space-y-6">
      <h1 className="text-2xl font-semibold mb-4">Dashboard</h1>

      {/* 1-col mobile / 2-col laptop grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* ─── Pie ───────────────────────────────────── */}
        <Card>
          <CardHeader>
            <CardTitle>
              Spend by Category&nbsp;(last&nbsp;30&nbsp;days)
            </CardTitle>
          </CardHeader>

          <CardContent className="flex flex-col items-center">
            <ChartContainer
              config={catConfig}
              className="min-h-[260px] w-full flex flex-col items-center"
            >
              <ResponsiveContainer height={200}>
                <PieChart accessibilityLayer>
                  <Pie
                    data={categories}
                    dataKey="totalAmount"
                    nameKey="category"
                    innerRadius={60}
                    outerRadius={80}
                    paddingAngle={1}
                    labelLine={false}
                  >
                    {categories.map((_, i) => (
                      <Cell key={i} fill={palette(i)} />
                    ))}
                  </Pie>
                  <ChartTooltip content={<ChartTooltipContent />} />
                </PieChart>
              </ResponsiveContainer>
            </ChartContainer>

            {/* custom legend strip */}
            <ul className="mt-4 flex flex-wrap justify-center gap-x-5 gap-y-2 text-sm text-muted-foreground">
              {categories.map((c, i) => (
                <li key={c.category} className="flex items-center gap-1.5">
                  <span
                    className="inline-block size-3 rounded-full"
                    style={{ backgroundColor: palette(i) }}
                  />
                  {c.category}
                  <span className="font-medium text-foreground">
                    {fmt(c.totalAmount)}
                  </span>
                </li>
              ))}
            </ul>
          </CardContent>
        </Card>

        {/* ─── Line ─────────────────────────────────── */}
        <Card>
          <CardHeader>
            <CardTitle>Daily Spending&nbsp;(last&nbsp;30&nbsp;days)</CardTitle>
          </CardHeader>

          <CardContent>
            <ChartContainer
              config={{
                totalAmount: { label: "Total", color: "var(--chart-1)" },
              }}
              className="min-h-[260px] w-full"
            >
              <ResponsiveContainer>
                <LineChart data={daily} accessibilityLayer>
                  <CartesianGrid vertical={false} strokeDasharray="3 3" />
                  <XAxis
                    dataKey="date"
                    tickLine={false}
                    axisLine={false}
                    fontSize={11}
                    tickFormatter={(d) => d.slice(5)}
                  />
                  <YAxis
                    tickLine={false}
                    axisLine={false}
                    fontSize={11}
                    tickFormatter={fmt}
                  />
                  <ChartTooltip content={<ChartTooltipContent />} />

                  <Line
                    type="monotone"
                    dataKey="totalAmount"
                    stroke="var(--chart-1)"
                    strokeWidth={2}
                    dot={false}
                    activeDot={{ r: 4 }}
                  />
                </LineChart>
              </ResponsiveContainer>
            </ChartContainer>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}
