import type { InferType } from "yup";

import type { registerSchema } from "@/validators/register-form-validator";

import { api, type ErrorResponse } from "@/lib/api";

import { sanitize } from "@/utils/santize";
import { toast } from "react-toastify";

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
	} catch (err: ErrorResponse | any) {
		toast.error("Erro ao registrar: " + (err.message || "Erro desconhecido"), {
			type: "error",
		});
		return false;
	}
}
