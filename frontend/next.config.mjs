/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      {
        source: "/api/:path*", // match all /api/* calls
        destination: "http://localhost:8080/api/:path*", // proxy to Spring Boot
      },
    ];
  },
};

export default nextConfig;
