import { api } from "@/lib/api";
import type { Transaction } from "@/types/transaction";
import { toast } from "react-toastify";

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
		await api("/transactions", requestInit);

		toast("Transação criada com sucesso!", { type: "success" });
		return true;
	} catch {
		toast("Erro ao criar transação", { type: "error" });
		return false;
	}
}
