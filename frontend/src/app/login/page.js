"use client";

import { useRouter } from "next/navigation";
import { useAuth, mutateAuth } from "@/hooks/use-auth";
import { useForm } from "react-hook-form";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import Link from "next/link";

export default function LoginPage() {
  const router = useRouter();
  const { authed, loading } = useAuth();
  const form = useForm({ defaultValues: { email: "", password: "" } });

  /* 1) still checking → nothing */
  if (loading) return null;

  /* 2) already signed-in → bounce */
  if (authed) {
    router.replace("/dashboard");
    return null;
  }

  /* 3) normal submit */
  async function onSubmit(values) {
    const res = await fetch("/api/auth/login", {
      method: "POST",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(values),
    });

    if (res.ok) {
      mutateAuth(true); // <────────── instant global update
      router.push("/dashboard"); // land on charts, not on Advice
    } else {
      const { error = "Login failed" } = await res.json().catch(() => ({}));
      form.setError("root.server", { message: error });
    }
  }

  /* ——— UI ——— */
  return (
    <main className="min-h-screen flex items-center justify-center p-6">
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full max-w-sm space-y-6 bg-card p-6 rounded-lg shadow"
        >
          <h1 className="text-2xl font-semibold">Log in to Pennywise</h1>

          {form.formState.errors.root?.server && (
            <p className="text-destructive text-sm">
              {form.formState.errors.root.server.message}
            </p>
          )}

          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input
                    type="email"
                    placeholder="you@example.com"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Password</FormLabel>
                <FormControl>
                  <Input type="password" placeholder="••••••••" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <Button type="submit" className="w-full">
            Log In
          </Button>

          <p className="text-sm text-muted-foreground text-center">
            Don’t have an account?{" "}
            <Link href="/register" className="underline underline-offset-4">
              Sign up
            </Link>
          </p>
        </form>
      </Form>
    </main>
  );
}
