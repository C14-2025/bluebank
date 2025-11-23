import { api } from "@/lib/api";

export async function deleteCustomer(id: string) {
	const requestInit = {
		method: "DELETE",
		headers: {
			"Content-Type": "application/json",
		},
	};

	try {
		const res = await api(`/customers/${id}`, requestInit);
		return res.ok;
	} catch (err: unknown) {
		const msg = err instanceof Error ? err.message : String(err);
		alert("Erro ao requisitar informações: " + (msg || "Erro desconhecido"));
		throw err;
	}
}
