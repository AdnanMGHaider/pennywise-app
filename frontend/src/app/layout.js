// frontend/src/app/layout.js
import "./globals.css";
import { ThemeProvider } from "@/components/ui/theme-provider";
import { Header } from "@/components/Header";

export const metadata = {
  title: "Pennywise",
  description: "AI-powered finance co-pilot",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body>
        {/* Global header with Shadcn navigation-menu, dropdown-menu, avatar & logout */}
        <Header />

        <ThemeProvider
          attribute="class"
          defaultTheme="system"
          enableSystem
          disableTransitionOnChange
        >
          {children}
        </ThemeProvider>
      </body>
    </html>
  );
}
