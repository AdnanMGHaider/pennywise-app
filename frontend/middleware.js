import { NextResponse } from "next/server";

export function middleware(request) {
  const { nextUrl, cookies } = request;
  const token = cookies.get("pennywise_jwt")?.value;

  const authPages = ["/login", "/register"];
  const protectedPages = [
    "/advice",
    "/saved-advice",
    "/dashboard",
    "/transactions",
    "/goals",
  ];

  /* —— Auth pages —— */
  if (authPages.some((p) => nextUrl.pathname.startsWith(p))) {
    // If user already has a token, bounce them to Advice
    if (token) {
      return NextResponse.redirect(new URL("/advice", request.url));
    }
    return NextResponse.next();
  }

  /* —— Protected pages —— */
  if (protectedPages.some((p) => nextUrl.pathname.startsWith(p)) && !token) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  return NextResponse.next();
}

/* apply to all relevant routes */
export const config = {
  matcher: [
    "/login",
    "/register",
    "/advice",
    "/dashboard",
    "/transactions",
    "/goals",
  ],
};
