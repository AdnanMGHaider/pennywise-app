"use client";

import Link from "next/link";
import { useAuth } from "@/hooks/use-auth";
import {
  NavigationMenu,
  NavigationMenuList,
  NavigationMenuItem,
  NavigationMenuLink,
} from "@/components/ui/navigation-menu";
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
} from "@/components/ui/dropdown-menu";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { LogoutButton } from "@/components/logout-button";

export function Header() {
  const { authed, loading } = useAuth();

  /* ---------- shared page list & label helper ---------- */
  const PAGES = [
    "dashboard",
    "transactions",
    "goals",
    "advice",
    "saved-advice",
  ];

  /** turn "saved-advice" → "Saved Advice" */
  const label = (slug) =>
    slug
      .split("-")
      .map((w) => w[0].toUpperCase() + w.slice(1))
      .join(" ");

  /* ---------- loading skeleton while /api/auth/status resolves ---------- */
  if (loading) {
    return (
      <header className="flex items-center justify-between p-4 bg-card">
        <span className="h-6 w-24 bg-muted rounded animate-pulse" />
        <span className="h-8 w-20 bg-muted rounded animate-pulse" />
      </header>
    );
  }

  return (
    <header className="flex items-center justify-between p-4 bg-card">
      <Link href="/" className="text-lg font-bold">
        Pennywise
      </Link>

      {/* ─── Desktop nav ─────────────────────────────────────────── */}
      {authed ? (
        <nav className="hidden md:block">
          <NavigationMenu>
            <NavigationMenuList className="flex space-x-4">
              {PAGES.map((p) => (
                <NavigationMenuItem key={p}>
                  <NavigationMenuLink href={`/${p}`}>
                    {label(p)}
                  </NavigationMenuLink>
                </NavigationMenuItem>
              ))}
            </NavigationMenuList>
          </NavigationMenu>
        </nav>
      ) : (
        <div className="hidden md:flex gap-2">
          <Link href="/login">
            <Button variant="outline">Log In</Button>
          </Link>
          <Link href="/register">
            <Button>Sign Up</Button>
          </Link>
        </div>
      )}

      {/* ─── Mobile nav (hamburger) ─────────────────────────────── */}
      <div className="flex items-center space-x-2 md:hidden">
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="outline" size="icon">
              ☰
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            {authed ? (
              <>
                {PAGES.map((p) => (
                  <DropdownMenuItem asChild key={p}>
                    <Link href={`/${p}`}>{label(p)}</Link>
                  </DropdownMenuItem>
                ))}
                <DropdownMenuItem>
                  <LogoutButton />
                </DropdownMenuItem>
              </>
            ) : (
              <>
                <DropdownMenuItem asChild>
                  <Link href="/login">Log In</Link>
                </DropdownMenuItem>
                <DropdownMenuItem asChild>
                  <Link href="/register">Sign Up</Link>
                </DropdownMenuItem>
              </>
            )}
          </DropdownMenuContent>
        </DropdownMenu>

        <Avatar className="size-8">
          <AvatarFallback>U</AvatarFallback>
        </Avatar>
      </div>

      {authed && (
        <div className="hidden md:block">
          <LogoutButton />
        </div>
      )}
    </header>
  );
}
