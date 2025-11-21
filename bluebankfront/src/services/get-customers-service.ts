import { api } from "@/lib/api";

import type { Customer } from "@/types/customer";
import type { GetCustomerResponse } from "./responses/get-customer-response";

export async function getCustomersList(page: number = 0): Promise<Customer[]> {
	const requestInit = {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	};

	try {
		const res = await api(`/customers?page=${page}`, requestInit);

		const { content } = (await res.json()) as GetCustomerResponse;

		return content;
	} catch (err: any) {
		alert(
			"Erro ao requisitar informações: " + (err?.message || "Erro desconhecido")
		);
		return [];
	}
}
