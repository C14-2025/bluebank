import { api } from "@/lib/api";
import { sanitize } from "@/utils/santize";
import type { registerSchema } from "@/validators/register-form-validator";
import type { InferType } from "yup";

type RegisterFormValues = InferType<typeof registerSchema>;

export async function updateCustomer(
	values: RegisterFormValues,
	id: string
): Promise<boolean> {
	const payload = sanitize(values);

	const requestInit = {
		method: "PUT",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(payload),
	};

	try {
		const res = await api(`/customers/${id}`, requestInit);

		return res.ok;
	} catch (err: unknown) {
		const msg = err instanceof Error ? err.message : String(err);
		alert("Erro ao registrar: " + (msg || "Erro desconhecido"));
		return false;
	}
}
