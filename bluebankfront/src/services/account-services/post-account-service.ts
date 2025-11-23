import { api, type ErrorResponse } from "@/lib/api";
import type { Account } from "@/types/account";
import { toast } from "react-toastify";

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
	} catch (err: ErrorResponse | any) {
		toast.error(
			"Erro ao registrar conta: " + (err.message || "Erro desconhecido"),
			{ type: "error" }
		);
		return false;
	}
}
