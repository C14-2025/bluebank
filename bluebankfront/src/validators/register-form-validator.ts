import * as Yup from "yup";
import type { StringSchema } from "yup";

export const registerSchema = Yup.object().shape({
	fullName: Yup.string().required("Campo obrigatório"),
	dob: Yup.string().required("Campo obrigatório"),
	nationality: Yup.string().required("Campo obrigatório"),
	countryCode: Yup.string()
		.matches(/^\+\d{1,3}$/, "Ex: +55")
		.required("Campo obrigatório"),
	phone: Yup.string()
		.matches(/^\(\d{2}\) \d{4,5}-\d{4}$/, "Telefone inválido")
		.required("Campo obrigatório"),
	email: Yup.string().email("Email inválido").required("Campo obrigatório"),
	occupation: Yup.string().required("Campo obrigatório"),
	docType: Yup.string().required("Campo obrigatório"),
	docNumber: Yup.string().when(
		"docType",
		(docType: unknown, schema: StringSchema) =>
			docType === "CPF"
				? schema
						.matches(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF inválido")
						.required("Campo obrigatório")
				: schema.required("Campo obrigatório")
	),
});
