import { api } from "@/lib/api";
import type { Account } from "@/types/account";

export async function createAccount(
	account: Omit<Account, "id">
): Promise<boolean> {
	const payload = account;

	const requestInit: RequestInit = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(payload),
	};

	try {
		const res = await api("/accounts", requestInit);

		return res.ok;
	} catch (err: unknown) {
		const msg = err instanceof Error ? err.message : String(err);
		alert("Erro ao registrar conta: " + (msg || "Erro desconhecido"));
		return false;
	}
}
