import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import type { Customer } from "@/types/customer";
import { getCustomer } from "@/services/customers-services/get-customers-service";
import { updateCustomer } from "@/services/customers-services/put-customer-service";
import { Pencil } from "lucide-react";

export function UserProfile() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [user, setUser] = useState<Customer | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [formFullName, setFormFullName] = useState<string>("");
  const [formEmail, setFormEmail] = useState<string>("");
  const [formDob, setFormDob] = useState<string>("");
  const [formNationality, setFormNationality] = useState<string>("");
  const [formOccupation, setFormOccupation] = useState<string>("");
  const [formCountryCode, setFormCountryCode] = useState<string>("");
  const [formPhone, setFormPhone] = useState<string>("");
  const [formDocType, setFormDocType] = useState<string>("");
  const [formDocNumber, setFormDocNumber] = useState<string>("");

  useEffect(() => {
    async function fetchData() {
      if (!id) return;
      setLoading(true);
      try {
        const res = await getCustomer(id);
        setUser(res);
      } catch (err) {
        console.error(err);
        setUser(null);
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, [id]);

  useEffect(() => {
    if (!user) return;
    setFormFullName(user.fullName ?? "");
    setFormEmail(user.email ?? "");
    setFormDob(user.dob ?? "");
    setFormNationality(user.nationality ?? "");
    setFormOccupation(user.occupation ?? "");
    setFormCountryCode(user.countryCode ?? "");
    setFormPhone(user.phone ?? "");
    setFormDocType(user.docType ?? "");
    setFormDocNumber(user.docNumber ?? "");
  }, [user, isEditing]);

  if (loading) return <div className="p-6">Carregando...</div>;
  if (!user) return <div className="p-6">Cliente não encontrado</div>;


  function handleRegisterAccount() {
    if (!user?.id) return;
    navigate(`/registeraccount/${user.id}`);
  }

  async function handleSave() {
    if (!user || !user.id) {
      alert("ID do Cliente não encontrado.");
      return;
    }

    const payload = {
      fullName: formFullName,
      dob: formDob,
      nationality: formNationality,
      countryCode: formCountryCode,
      phone: formPhone,
      email: formEmail,
      occupation: formOccupation,
      docType: formDocType,
      docNumber: formDocNumber,
    };

    try {
      await updateCustomer(payload, user.id);
      setUser((prev) => (prev ? { ...prev, ...payload } as Customer : prev));
      setIsEditing(false);
      alert("Alterações salvas.");
    } catch (err) {
      console.error(err);
      alert("Erro ao salvar alterações.");
    }
  }

  function handleCancelEdit() {
    setIsEditing(false);
    if (user) {
      setFormFullName(user.fullName ?? "");
      setFormEmail(user.email ?? "");
      setFormDob(user.dob ?? "");
      setFormNationality(user.nationality ?? "");
      setFormOccupation(user.occupation ?? "");
      setFormCountryCode(user.countryCode ?? "");
      setFormPhone(user.phone ?? "");
      setFormDocType(user.docType ?? "");
      setFormDocNumber(user.docNumber ?? "");
    }
  }

  return (
    <div className="flex justify-center h-screen gap-6">
      <div className="w-[800px] h-[680px] mt-10 p-8 bg-white rounded-xl shadow-2xl flex flex-col box-border overflow-hidden text-lg">
        <div>
          <button
            type="button"
            className="mb-4 text-md font-semibold text-white bg-blue-600 px-6 py-2 rounded cursor-pointer"
            onClick={() => navigate(-1)}
          >
            Voltar
          </button>
        </div>

        <div className="flex flex-1 justify-center items-center overflow-auto">
          <div className="w-full max-w-[700px] p-4">
            <div className="flex justify-between items-start">
              {isEditing ? (
                <h2 className="text-2xl font-bold mb-4">Editar Cliente</h2>
              ) : (
                <h2 className="text-2xl font-bold mb-4">{user.fullName}</h2>
              )}

              {!isEditing ? (
                <button
                  type="button"
                  className="bg-blue-600 w-10 h-10 p-2.5 rounded-lg flex items-center justify-center transition cursor-pointer hover:scale-110"
                  onClick={() => setIsEditing(true)}
                >
                  <Pencil className="text-white" />
                </button>
              ) : (
                <div className="flex gap-2">
                  <button type="button" className="bg-blue-600 text-white px-3 py-1 rounded-lg cursor-pointer" onClick={handleSave}>
                    Salvar
                  </button>
                  <button type="button" className="bg-gray-300 text-black px-3 py-1 rounded-lg cursor-pointer" onClick={handleCancelEdit}>
                    Cancelar
                  </button>
                </div>
              )}
            </div>

            {isEditing ? (
              <div className="space-y-3 mt-4">
                <div>
                  <label className="block text-sm font-medium">Nome</label>
                  <input
                    className="w-full px-3 py-2 border rounded-lg"
                    value={formFullName}
                    onChange={(e) => setFormFullName(e.target.value)}
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium">Email</label>
                  <input
                    className="w-full px-3 py-2 border rounded-lg"
                    value={formEmail}
                    onChange={(e) => setFormEmail(e.target.value)}
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium">Data de Nasc</label>
                    <input
                      type="date"
                      className="w-full px-3 py-2 border rounded-lg"
                      value={formDob}
                      onChange={(e) => setFormDob(e.target.value)}
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium">Nacionalidade</label>
                    <input
                      className="w-full px-3 py-2 border rounded-lg"
                      value={formNationality}
                      onChange={(e) => setFormNationality(e.target.value)}
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium">Ocupação</label>
                  <input
                    className="w-full px-3 py-2 border rounded-lg"
                    value={formOccupation}
                    onChange={(e) => setFormOccupation(e.target.value)}
                  />
                </div>

                <div className="grid grid-cols-3 gap-4">
                  <div>
                    <label className="block text-sm font-medium">Código do País</label>
                    <input
                      className="w-full px-3 py-2 border rounded-lg"
                      value={formCountryCode}
                      onChange={(e) => setFormCountryCode(e.target.value)}
                    />
                  </div>
                  <div className="col-span-2">
                    <label className="block text-sm font-medium">Telefone</label>
                    <input
                      className="w-full px-3 py-2 border rounded-lg"
                      value={formPhone}
                      onChange={(e) => setFormPhone(e.target.value)}
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium">Tipo de Documento</label>
                    <select className="w-full px-3 py-2 border rounded-lg" value={formDocType} onChange={(e) => setFormDocType(e.target.value)}>
                      <option value="">Selecione</option>
                      <option value="CPF">CPF</option>
                      <option value="RG">RG</option>
                      <option value="Passaporte">Passaporte</option>
                    </select>
                  </div>
                  <div>
                    <label className="block text-sm font-medium">Número do Documento</label>
                    <input
                      className="w-full px-3 py-2 border rounded-lg"
                      value={formDocNumber}
                      onChange={(e) => setFormDocNumber(e.target.value)}
                    />
                  </div>
                </div>
              </div>
            ) : (
              <div className="space-y-2 mt-4">
                <div><strong>ID:</strong> {user.id}</div>
                <div><strong>Email:</strong> {user.email}</div>
                <div><strong>Telefone:</strong> {user.countryCode ? `${user.countryCode} ` : ""}{user.phone ?? ""}</div>
                <div><strong>Doc Tipo:</strong> {user.docType ?? '-'}</div>
                <div><strong>Doc Número:</strong> {user.docNumber ?? '-'}</div>
                <div><strong>Data de Nasc:</strong> {user.dob ?? '-'}</div>
                <div><strong>Nacionalidade:</strong> {user.nationality ?? '-'}</div>
                <div><strong>Ocupação:</strong> {user.occupation ?? '-'}</div>
              </div>
            )}
          </div>
        </div>
      </div>
      <div className="w-[550px] h-[680px] mt-10 p-8 shadow-2xl bg-linear-to-r from-cyan-500 to-blue-600 rounded-xl flex flex-col box-border overflow-hidden text-lg">
        <div className="flex flex-col justify-between bg-linear-to-r from-cyan-500 to-blue-600 rounded-2xl h-full">
          <div className="mx-10 mt-10 flex flex-1 flex-col justify-between">
            <h3 className="font-medium antialiased text-4xl text-white">Você está a um passo de transformar sua experiência</h3>
            <p className="font-semibold antialiased text-3xl text-white">Clique no botão abaixo e comece agora!</p>
          </div>
          <button 
          className="flex flex-row justify-center m-3 bg-linear-to-r from-emerald-700 to-lime-500 text-3xl gap-3 p-6 rounded-xl shadow-xl cursor-pointer hover:scale-105 transition-transform"
          onClick={handleRegisterAccount}
          >
            <h1 className="font-medium text-shadow-lg text-white">Crie sua conta </h1>
            <strong className="bg-linear-to-r font-extrabold text-white text-shadow-lg  ">AGORA!</strong>
          </button>
        </div>
      </div>
    </div>
  );
}