import { useNavigate, useParams } from "react-router-dom";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";

import { createAccount } from "@/services/account-services/post-account-service";
import { toast } from "react-toastify";

type AccountFormValues = {
	accountNumber: string;
	balance: string;
	branchCode: string;
};

const accountRegisterSchema = Yup.object({
	accountNumber: Yup.string()
		.required("Número da conta obrigatório")
		.matches(/^\d{8,12}$/, "Número da conta deve ter entre 8 e 12 dígitos"),
	balance: Yup.number()
		.typeError("Saldo deve ser numérico, use ponto (.) para centavos")
		.required("Saldo obrigatório")
		.min(0, "Saldo não pode ser negativo"),
	branchCode: Yup.number()
		.typeError("Agência deve ser numérica")
		.required("Código da agência obrigatório")
		.min(1, "Agência deve ser entre 1 e 999")
		.max(999, "Agência deve ser entre 1 e 999"),
});

export function RegisterAccountPage() {
	const navigate = useNavigate();
	const { customerId } = useParams<{ customerId: string }>(); // ajuste o nome conforme sua rota

	const handleSubmitForm = async (values: AccountFormValues) => {
		if (!customerId) {
			toast.error("Cliente não informado para vincular a conta.", {
				type: "error",
			});
			return;
		}

		const normalizedBalance = values.balance.replace(",", ".");

		const ok = await createAccount({
			accountNumber: values.accountNumber.trim(),
			balance: Number(normalizedBalance),
			branchCode: Number(values.branchCode),
			customerId,
		});

		if (!ok) return;

		navigate(`/users/${customerId}`);
	};

	return (
		<div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-xl shadow-lg">
			<h2 className="text-center text-2xl font-bold mb-6 text-blue-700">
				Registro de Conta
			</h2>

			<Formik<AccountFormValues>
				initialValues={{
					accountNumber: "",
					balance: "",
					branchCode: "",
				}}
				validationSchema={accountRegisterSchema}
				onSubmit={handleSubmitForm}
			>
				{() => (
					<Form className="space-y-4">
						<div>
							<label className="block text-sm font-medium text-gray-700">
								Número da conta
							</label>
							<Field
								name="accountNumber"
								type="text"
								className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
							/>
							<ErrorMessage
								name="accountNumber"
								component="div"
								className="text-red-500 text-xs"
							/>
						</div>

						<div>
							<label className="block text-sm font-medium text-gray-700">
								Saldo inicial
							</label>
							<Field
								name="balance"
								type="text"
								className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
								placeholder="0,00"
							/>
							<ErrorMessage
								name="balance"
								component="div"
								className="text-red-500 text-xs"
							/>
						</div>

						<div>
							<label className="block text-sm font-medium text-gray-700">
								Código da agência
							</label>
							<Field
								name="branchCode"
								type="text"
								className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
							/>
							<ErrorMessage
								name="branchCode"
								component="div"
								className="text-red-500 text-xs"
							/>
						</div>

						<button
							type="submit"
							className="w-full py-2 px-4 bg-blue-600 text-white rounded-lg font-semibold hover:bg-blue-700 transition"
						>
							Registrar conta
						</button>
					</Form>
				)}
			</Formik>
		</div>
	);
}
