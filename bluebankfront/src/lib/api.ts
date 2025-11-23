import { API_BASE_URL } from "@/config/constants";

export interface ErrorResponse {
	status: number;
	message: string;
}

export async function api(path: string, init?: RequestInit) {
	const requestInfo = API_BASE_URL
		? new URL(path, API_BASE_URL).toString()
		: path;

	const res = await fetch(requestInfo, init);

	if (!res.ok) {
		const errorBody = await safeJson(res);

		throw {
			status: errorBody?.status || res.status,
			message: errorBody?.message || "Erro na requisição",
		};
	}

	return res;
}

async function safeJson(res: Response): Promise<ErrorResponse | null> {
	try {
		return await res.json();
	} catch {
		return null;
	}
}
