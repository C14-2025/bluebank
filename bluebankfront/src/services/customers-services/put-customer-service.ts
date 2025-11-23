import { api } from "@/lib/api";
import { sanitize } from "@/utils/santize";
import type { registerSchema } from "@/validators/register-form-validator";
import type { ErrorResponse } from "@/lib/api";
import type { InferType } from "yup";
import { toast } from "react-toastify";

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
	} catch (err: ErrorResponse | any) {
		toast.error("Erro ao registrar: " + (err.message || "Erro desconhecido"), {
			type: "error",
		});
		return false;
	}
}
