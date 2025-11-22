import { api } from "@/lib/api";

import type { GetCustomerResponse } from "./responses/get-customer-response";

export async function getCustomersList(
	page: number = 0
): Promise<GetCustomerResponse> {
	const requestInit = {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	};

	try {
		const res = await api(`/customers?page=${page}`, requestInit);

		const content = (await res.json()) as GetCustomerResponse;
		return content;
	} catch (err: any) {
		alert(
			"Erro ao requisitar informações: " + (err?.message || "Erro desconhecido")
		);
		throw err;
	}
}

export async function getCustomersListByDoc(
	userDocFilter: string,
	userDocTypeFilter: string
): Promise<GetCustomerResponse> {
	const requestInit = {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	};

	try {
		const res = await api(
			`/customers/by-doc?doc-type=${userDocTypeFilter}&doc-number=${userDocFilter}`,
			requestInit
		);

		const content = (await res.json()) as GetCustomerResponse;
		return content;
	} catch (err: any) {
		alert(
			"Erro ao requisitar informações: " + (err?.message || "Erro desconhecido")
		);
		throw err;
	}
}

export async function searchCustomers(
	params: Record<string, string | number | undefined> = {}
): Promise<GetCustomerResponse> {
	const requestInit = {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	};

	// Remove undefined or empty params
	const cleaned: Record<string, string> = {};
	Object.entries(params).forEach(([k, v]) => {
		if (v === undefined || v === null) return;
		const s = String(v);
		if (s.trim() === "") return;
		cleaned[k] = s;
	});

	const qs = new URLSearchParams(cleaned).toString();
	const url = qs ? `/customers?${qs}` : `/customers`;

	try {
		const res = await api(url, requestInit);
		const json = (await res.json()) as GetCustomerResponse;
		return json;
	} catch (err: any) {
		alert(
			"Erro ao requisitar informações: " + (err?.message || "Erro desconhecido")
		);
		throw err;
	}
}
