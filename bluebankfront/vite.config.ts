import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import { resolve } from "path";

// https://vite.dev/config/
export default defineConfig({
	plugins: [react(), tailwindcss()],
	resolve: {
		alias: {
			"@": resolve(__dirname, "src"),
		},
	},
	server: {
		proxy: {
			"/customers": {
				target: "http://localhost:8080",
				changeOrigin: true,
				secure: false,
			},
			"/accounts": "http://localhost:8080",
			"/customers/by-doc": {
				target: "http://localhost:8080",
				changeOrigin: true,
				secure: false,
			},
			"/transactions": "http://localhost:8080",
		},
	},
});
