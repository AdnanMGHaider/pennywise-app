"use client";

import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { mutateAuth } from "@/hooks/use-auth";

export function LogoutButton() {
  const router = useRouter();

  async function handleLogout() {
    // 1) ask the backend to clear the cookie
    await fetch("/api/auth/logout", {
      method: "POST",
      credentials: "include",
    });
    // 2) update the shared auth cache *right now*
    mutateAuth(false);
    // 3) back to the public home page
    router.replace("/");
  }

  return (
    <Button variant="outline" onClick={handleLogout}>
      Sign Out
    </Button>
  );
}
