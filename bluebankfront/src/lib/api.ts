import { API_BASE_URL } from "@/config/constants";

export function api<T>(path: string, init?: RequestInit) {
	const url = new URL(path, API_BASE_URL);

	return fetch(url, init) as Promise<Response>;
}
