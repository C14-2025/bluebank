import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import type { Customer } from "@/types/customer";

export function UserProfile() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [user, setUser] = useState<Customer | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    setLoading(true);
    fetch(`/customers/${id}`)
      .then(async (res) => {
        if (!res.ok) throw new Error(await res.text());
        return res.json();
      })
      .then((data) => setUser(data))
      .catch((err) => setError(String(err)))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <div className="p-6">Carregando...</div>;
  if (error) return <div className="p-6 text-red-600">Erro: {error}</div>;
  if (!user) return <div className="p-6">Usuário não encontrado</div>;

  return (
    <div className="max-w-lg mx-auto mt-10 p-6 bg-white rounded-xl shadow-lg">
      <button className="mb-4 text-sm text-blue-600" onClick={() => navigate(-1)}>Voltar</button>
      <h2 className="text-2xl font-bold mb-4">{user.fullName}</h2>
      <div className="space-y-2">
        <div><strong>Email:</strong> {user.email}</div>
        <div><strong>ID:</strong> {user.id}</div>
        <div><strong>Data de Nasc:</strong> {user.dob ?? '-'}</div>
        <div><strong>Nacionalidade:</strong> {user.nationality ?? '-'}</div>
        <div><strong>Ocupação:</strong> {user.occupation ?? '-'}</div>
        {/* adicionar outros campos conforme Customer type */}
      </div>
    </div>
  );
}