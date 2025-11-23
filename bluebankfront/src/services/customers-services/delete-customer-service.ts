import { api, type ErrorResponse } from "@/lib/api";
import { toast } from "react-toastify";

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
	} catch (err: ErrorResponse | any) {
		toast.error(
			"Erro ao requisitar informações: " + (err.message || "Erro desconhecido"),
			{ type: "error" }
		);
		throw err;
	}
}
