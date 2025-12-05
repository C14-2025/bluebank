import { useEffect, useState } from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";

import type { Customer } from "@/types/customer";
import type { Account } from "@/types/account";

import { fetchCustomers } from "@/services/customers-services/get-customers-service";
import { getAccountByCustomerDocument } from "@/services/account-services/get-account-by-customer-service";
import { createTransaction } from "@/services/transactions-services/post-transaction-service";
import { toast } from "react-toastify";

const TransactionSchema = Yup.object().shape({
	payerId: Yup.string().required("Selecione um cliente pagador."),
	payeeId: Yup.string().required("Selecione um cliente recebedor."),
	amount: Yup.number()
		.typeError("Informe um valor válido.")
		.positive("O valor deve ser maior que zero.")
		.required("Informe o valor da transferência."),
});

export function TransactionsPage() {
	const [customers, setCustomers] = useState<Customer[]>([]);
	const [payerAccount, setPayerAccount] = useState<Account | null>(null);
	const [payeeAccount, setPayeeAccount] = useState<Account | null>(null);

	useEffect(() => {
		async function loadCustomers() {
			try {
				const res = await fetchCustomers(0);
				setCustomers(res.content ?? []);
			} catch (err) {
				console.error(err);
			}
		}

		loadCustomers();
	}, []);

	async function handleAccountLoad(
		customerId: string,
		setAccount: (account: Account | null) => void
	) {
		if (!customerId) {
			setAccount(null);
			return;
		}

		const customer = customers.find((c) => c.id === customerId);
		if (!customer || !customer.docType || !customer.docNumber) {
			setAccount(null);
			return;
		}

		try {
			const account = await getAccountByCustomerDocument(
				customer.docType,
				customer.docNumber
			);
			setAccount(account);
		} catch (err) {
			console.error(err);
			setAccount(null);
		}
	}

	return (
		<div className="p-6 max-w-4xl mx-auto">
			<h1 className="text-3xl font-bold mb-4 text-blue-700">Transações</h1>
			<p className="mb-6 text-gray-700">
				Simule uma transferência escolhendo o cliente pagador e o cliente
				recebedor.
			</p>

			<Formik
				initialValues={{ payerId: "", payeeId: "", amount: "" }}
				validationSchema={TransactionSchema}
				onSubmit={async (values, { setSubmitting, resetForm }) => {
					const { payerId, payeeId, amount } = values;

					const payer = customers.find((c) => c.id === payerId);
					const payee = customers.find((c) => c.id === payeeId);

					if (!payer?.email || !payee?.email) {
						toast.error(
							"Não foi possível localizar os emails dos clientes selecionados."
						);
						setSubmitting(false);
						return;
					}

					try {
						await createTransaction({
							amount: Number(amount),
							payerEmail: payer.email,
							payeeEmail: payee.email,
						});

						resetForm();
					} catch (error) {
					} finally {
						setSubmitting(false);
					}
				}}
			>
				{({ setFieldValue, isSubmitting }) => (
					<Form className="grid grid-cols-1 md:grid-cols-2 gap-6 bg-white rounded-xl shadow-lg p-6">
						<div>
							<h2 className="text-xl font-semibold mb-2 text-blue-700">
								Quem envia
							</h2>
							<label className="block text-sm font-medium text-gray-700 mb-1">
								Cliente
							</label>
							<Field
								as="select"
								name="payerId"
								className="w-full px-3 py-2 border rounded-lg"
								onChange={(e: React.ChangeEvent<HTMLSelectElement>) => {
									const value = e.target.value;
									setFieldValue("payerId", value);
									handleAccountLoad(value, setPayerAccount);
								}}
							>
								<option value="">Selecione um cliente</option>
								{customers.map((c) => (
									<option key={c.id} value={c.id}>
										{c.fullName}
									</option>
								))}
							</Field>
							<ErrorMessage
								name="payerId"
								component="div"
								className="text-red-500 text-sm mt-1"
							/>

							{payerAccount && (
								<div className="mt-4 text-sm text-gray-800 space-y-1">
									<div>
										<strong>Número da conta:</strong>{" "}
										{payerAccount.accountNumber}
									</div>
									<div>
										<strong>Código da agência:</strong>{" "}
										{payerAccount.branchCode}
									</div>
									<div>
										<strong>Saldo:</strong> R${" "}
										{payerAccount.balance != null
											? payerAccount.balance.toFixed(2)
											: "0,00"}
									</div>
								</div>
							)}
						</div>

						<div>
							<h2 className="text-xl font-semibold mb-2 text-blue-700">
								Quem recebe
							</h2>
							<label className="block text-sm font-medium text-gray-700 mb-1">
								Cliente
							</label>
							<Field
								as="select"
								name="payeeId"
								className="w-full px-3 py-2 border rounded-lg"
								onChange={(e: React.ChangeEvent<HTMLSelectElement>) => {
									const value = e.target.value;
									setFieldValue("payeeId", value);
									handleAccountLoad(value, setPayeeAccount);
								}}
							>
								<option value="">Selecione um cliente</option>
								{customers.map((c) => (
									<option key={c.id} value={c.id}>
										{c.fullName}
									</option>
								))}
							</Field>
							<ErrorMessage
								name="payeeId"
								component="div"
								className="text-red-500 text-sm mt-1"
							/>

							{payeeAccount && (
								<div className="mt-4 text-sm text-gray-800 space-y-1">
									<div>
										<strong>Número da conta:</strong>{" "}
										{payeeAccount.accountNumber}
									</div>
									<div>
										<strong>Código da agência:</strong>{" "}
										{payeeAccount.branchCode}
									</div>
									<div>
										<strong>Saldo:</strong> R${" "}
										{payeeAccount.balance != null
											? payeeAccount.balance.toFixed(2)
											: "0,00"}
									</div>
								</div>
							)}
						</div>

						<div className="md:col-span-2 mt-4">
							<label className="block text-sm font-medium text-gray-700 mb-1">
								Valor da transferência
							</label>
							<Field
								type="text"
								name="amount"
								className="w-full max-w-xs px-3 py-2 border rounded-lg"
								placeholder="0,00"
							/>
							<ErrorMessage
								name="amount"
								component="div"
								className="text-red-500 text-sm mt-1"
							/>

							<button
								type="submit"
								className="mt-4 px-6 py-2 bg-blue-600 text-white rounded-lg font-semibold hover:bg-blue-700 transition disabled:opacity-60"
								disabled={isSubmitting}
							>
								{isSubmitting ? "Enviando..." : "Simular transferência"}
							</button>
						</div>
					</Form>
				)}
			</Formik>
		</div>
	);
}
