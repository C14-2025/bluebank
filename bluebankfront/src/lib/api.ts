import { API_BASE_URL } from "@/config/constants";

export function api<T>(path: string, init?: RequestInit) {
	// When API_BASE_URL is empty, use relative paths so Vite dev proxy can forward requests.
	const requestInfo: string = API_BASE_URL
		? new URL(path, API_BASE_URL).toString()
		: path;

	return fetch(requestInfo, init) as Promise<Response>;
}
