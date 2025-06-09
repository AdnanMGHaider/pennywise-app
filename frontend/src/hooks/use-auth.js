"use client";

import useSWR, { mutate } from "swr";

/* always hit the same endpoint with credentials */
const fetcher = (url) =>
  fetch(url, { credentials: "include" }).then((r) => r.json());

/** shared SWR key */
const KEY = "/api/auth/status";

/**
 * useAuth()
 * ---------
 * { authed, loading } everywhere in the app – cached & shared.
 */
export function useAuth() {
  const { data, isLoading } = useSWR(KEY, fetcher, {
    refreshWhenHidden: false,
    refreshWhenOffline: false,
  });

  return {
    authed: data ? data.authenticated : false,
    loading: isLoading,
  };
}

/**
 * mutateAuth(next) – imperatively flip the cache and revalidate in background.
 *   – next === true  ➜ immediately tell everyone “you’re authed”
 *   – next === false ➜ immediately tell everyone “you’re logged-out”
 */
export function mutateAuth(next) {
  mutate(KEY, { authenticated: next }, { revalidate: false });
  // background re-fetch keeps it honest
}
