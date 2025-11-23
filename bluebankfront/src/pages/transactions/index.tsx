import { useEffect, useState } from "react";

import type { Customer } from "@/types/customer";
import type { Account } from "@/types/account";

import { fetchCustomers } from "@/services/customers-services/get-customers-service";
import { getAccountByCustomerDocument } from "@/services/account-services/get-account-by-customer-service";
import { createTransaction } from "@/services/transactions-services/post-transaction-service";

export function TransactionsPage() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loadingCustomers, setLoadingCustomers] = useState<boolean>(true);
  const [selectedPayerId, setSelectedPayerId] = useState<string>("");
  const [selectedPayeeId, setSelectedPayeeId] = useState<string>("");
  const [payerAccount, setPayerAccount] = useState<Account | null>(null);
  const [payeeAccount, setPayeeAccount] = useState<Account | null>(null);
  const [amount, setAmount] = useState<string>("");
  const [submitting, setSubmitting] = useState<boolean>(false);

  useEffect(() => {
    async function loadCustomers() {
      setLoadingCustomers(true);
      try {
        const res = await fetchCustomers(0);
        setCustomers(res.content ?? []);
      } catch (err) {
        console.error(err);
      } finally {
        setLoadingCustomers(false);
      }
    }

    loadCustomers();
  }, []);

  useEffect(() => {
    async function loadPayerAccount() {
      if (!selectedPayerId) {
        setPayerAccount(null);
        return;
      }

      const payer = customers.find((c) => c.id === selectedPayerId);
      if (!payer || !payer.docType || !payer.docNumber) {
        setPayerAccount(null);
        return;
      }
      try {
        const account = await getAccountByCustomerDocument(payer.docType, payer.docNumber);
        setPayerAccount(account);
      } catch (err) {
        console.error(err);
        setPayerAccount(null);
      }
    }

    loadPayerAccount();
  }, [selectedPayerId, customers]);

  useEffect(() => {
    async function loadPayeeAccount() {
      if (!selectedPayeeId) {
        setPayeeAccount(null);
        return;
      }

      const payee = customers.find((c) => c.id === selectedPayeeId);
      if (!payee || !payee.docType || !payee.docNumber) {
        setPayeeAccount(null);
        return;
      }
      try {
        const account = await getAccountByCustomerDocument(payee.docType, payee.docNumber);
        setPayeeAccount(account);
      } catch (err) {
        console.error(err);
        setPayeeAccount(null);
      }
    }

    loadPayeeAccount();
  }, [selectedPayeeId, customers]);

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();

    if (!payerAccount || !payeeAccount) {
      alert("Selecione remetente e destinatário com contas válidas.");
      return;
    }

    const value = Number(amount.replace(",", "."));
    if (Number.isNaN(value) || value <= 0) {
      alert("Informe um valor válido para transferência.");
      return;
    }

    const payer = customers.find((c) => c.id === selectedPayerId);
    const payee = customers.find((c) => c.id === selectedPayeeId);

    if (!payer?.email || !payee?.email) {
      alert("Não foi possível localizar os emails dos clientes selecionados.");
      return;
    }

    setSubmitting(true);
    const ok = await createTransaction({
      amount: value,
      payerEmail: payer.email,
      payeeEmail: payee.email,
    });
    setSubmitting(false);

    if (!ok) return;

    alert("Transação criada com sucesso.");
    setAmount("");
  }

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-4 text-blue-700">Transações</h1>
      <p className="mb-6 text-gray-700">
        Simule uma transferência escolhendo o cliente pagador e o cliente recebedor.
      </p>

      <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6 bg-white rounded-xl shadow-lg p-6">
        <div>
          <h2 className="text-xl font-semibold mb-2 text-blue-700">Quem envia</h2>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Cliente
          </label>
          <select
            className="w-full px-3 py-2 border rounded-lg"
            value={selectedPayerId}
            onChange={(e) => setSelectedPayerId(e.target.value)}
            disabled={loadingCustomers}
          >
            <option value="">Selecione um cliente</option>
            {customers.map((c) => (
              <option key={c.id} value={c.id}>
                {c.fullName}
              </option>
            ))}
          </select>

          {payerAccount && (
            <div className="mt-4 text-sm text-gray-800 space-y-1">
              <div>
                <strong>Número da conta:</strong> {payerAccount.accountNumber}
              </div>
              <div>
                <strong>Código da agência:</strong> {payerAccount.branchCode}
              </div>
              <div>
                <strong>Saldo:</strong>{" "}
                R$
                {" "}
                {payerAccount.balance != null
                  ? payerAccount.balance.toFixed(2)
                  : "0,00"}
              </div>
            </div>
          )}
        </div>

        <div>
          <h2 className="text-xl font-semibold mb-2 text-blue-700">Quem recebe</h2>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Cliente
          </label>
          <select
            className="w-full px-3 py-2 border rounded-lg"
            value={selectedPayeeId}
            onChange={(e) => setSelectedPayeeId(e.target.value)}
            disabled={loadingCustomers}
          >
            <option value="">Selecione um cliente</option>
            {customers.map((c) => (
              <option key={c.id} value={c.id}>
                {c.fullName}
              </option>
            ))}
          </select>

          {payeeAccount && (
            <div className="mt-4 text-sm text-gray-800 space-y-1">
              <div>
                <strong>Número da conta:</strong> {payeeAccount.accountNumber}
              </div>
              <div>
                <strong>Código da agência:</strong> {payeeAccount.branchCode}
              </div>
              <div>
                <strong>Saldo:</strong>{" "}
                R$
                {" "}
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
          <input
            type="text"
            className="w-full max-w-xs px-3 py-2 border rounded-lg"
            placeholder="0,00"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          />

          <button
            type="submit"
            className="mt-4 px-6 py-2 bg-blue-600 text-white rounded-lg font-semibold hover:bg-blue-700 transition disabled:opacity-60"
            disabled={submitting}
          >
            {submitting ? "Enviando..." : "Simular transferência"}
          </button>
        </div>
      </form>
    </div>
  );
}
