// Use `VITE_API_BASE_URL` to allow switching between direct backend and Vite proxy.
// Leave undefined or empty to use relative paths (so Vite proxy works).
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "";
