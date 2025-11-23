import type { InferType } from "yup";

import type { registerSchema } from "@/validators/register-form-validator";

import { api } from "@/lib/api";

import { sanitize } from "@/utils/santize";

type RegisterFormValues = InferType<typeof registerSchema>;

export async function createCustomer(
	values: RegisterFormValues
): Promise<boolean> {
	const payload = sanitize(values);

	const requestInit = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(payload),
	};

	try {
		const res = await api(`/customers`, requestInit);

		return res.ok;
	} catch (err: unknown) {
		const msg = err instanceof Error ? err.message : String(err);
		alert("Erro ao registrar: " + (msg || "Erro desconhecido"));
		return false;
	}
}
