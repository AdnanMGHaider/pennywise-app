"use client";

import { useRouter } from "next/navigation";
import { useAuth } from "@/hooks/use-auth";
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

export default function RegisterPage() {
  const router = useRouter();
  const { authed, loading } = useAuth(); // ← NEW
  const form = useForm({ defaultValues: { email: "", password: "" } });

  /* 1️⃣  still checking auth → render nothing */
  if (loading) return null;

  /* 2️⃣  already signed-in → bounce to /advice */
  if (authed) {
    router.replace("/advice");
    return null;
  }

  /* 3️⃣  regular submit */
  async function onSubmit(values) {
    const res = await fetch("/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(values),
    });

    if (res.ok) {
      router.push("/login");
    } else {
      const { error = "Registration failed" } = await res
        .json()
        .catch(() => ({}));
      form.setError("root.server", { message: error });
    }
  }

  /* ————— UI ————— */
  return (
    <main className="min-h-screen flex items-center justify-center p-6">
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full max-w-sm space-y-6 bg-card p-6 rounded-lg shadow"
        >
          <h1 className="text-2xl font-semibold">Create an Account</h1>

          {form.formState.errors.root?.server && (
            <p className="text-destructive text-sm">
              {form.formState.errors.root.server.message}
            </p>
          )}

          <FormField
            control={form.control}
            name="email"
            rules={{ required: "Email is required" }}
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
            rules={{ required: "Password is required", minLength: 6 }}
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
            Sign Up
          </Button>

          <p className="text-sm text-muted-foreground text-center">
            Already have an account?{" "}
            <Link href="/login" className="underline underline-offset-4">
              Log in
            </Link>
          </p>
        </form>
      </Form>
    </main>
  );
}
