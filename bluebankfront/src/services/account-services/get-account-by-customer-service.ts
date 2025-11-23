import { api, type ErrorResponse } from "@/lib/api";
import type { Account } from "@/types/account";
import { toast } from "react-toastify";

export async function getAccountByCustomerDocument(
	docType: string,
	docNumber: string
): Promise<Account> {
	const requestInit: RequestInit = {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	};

	// Monta querystring com tipo e número de documento
	const params = new URLSearchParams({
		"doc-type": docType,
		"doc-number": docNumber,
	});

	try {
		const res = await api(
			`/accounts/by-doc?doc-type=${params.get(
				"doc-type"
			)}&doc-number=${params.get("doc-number")}`,
			requestInit
		);
		const content = (await res.json()) as Account;
		return content;
	} catch (err: ErrorResponse | any) {
		toast.error(
			"Erro ao buscar conta do cliente: " +
				(err.message || "Erro desconhecido"),
			{ type: "error" }
		);
		throw err;
	}
}
