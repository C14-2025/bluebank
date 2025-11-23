import { api } from "@/lib/api";
import type { Transaction } from "@/types/transaction";

export async function createTransaction(
	transaction: Omit<Transaction, "id">
): Promise<boolean> {
	const payload = transaction;

	const requestInit: RequestInit = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(payload),
	};

	try {
		const res = await api("/transactions", requestInit);
		if (res.ok) {
			return res.ok;
		} else {
			const { message } = await res.json();
			alert("Erro ao criar transação: " + (message || "Erro desconhecido"));
			return false;
		}
	} catch {
		alert("Erro ao criar transação");
		return false;
	}
}
