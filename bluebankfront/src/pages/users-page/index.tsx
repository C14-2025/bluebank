
import { useState } from "react";
import { useNavigate } from "react-router";
import { Pencil } from "lucide-react";
import { Trash } from "lucide-react"


const users = [
  { id: 1, name: "Ana Souza", email: "ana.souza@email.com" },
  { id: 2, name: "Carlos Silva", email: "carlos.silva@email.com" },
  { id: 3, name: "Mariana Lima", email: "mariana.lima@email.com" },
  { id: 4, name: "João Pereira", email: "joao.pereira@email.com" },
  { id: 5, name: "Lucas Alves", email: "lucas.alves@email.com" },
  { id: 6, name: "Fernanda Costa", email: "fernanda.costa@email.com" },
  { id: 7, name: "Pedro Ramos", email: "pedro.ramos@email.com" },
  { id: 8, name: "Juliana Torres", email: "juliana.torres@email.com" },
];

const USERS_PER_PAGE = 4;

export function UsersPage() {
  const [page, setPage] = useState(1);
  const totalPages = Math.ceil(users.length / USERS_PER_PAGE);
  const paginatedUsers = users.slice((page - 1) * USERS_PER_PAGE, page * USERS_PER_PAGE);
  const navigate = useNavigate();
  
  function handlePrev() {
    setPage((p) => Math.max(1, p - 1));
  }
  function handleNext() {
    setPage((p) => Math.min(totalPages, p + 1));
  }
  
  function handleRegister() {
    navigate('/register');
  }

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-xl shadow-lg">
      <h2 className="text-center text-2xl font-bold mb-6">Lista de Usuários</h2>
      <button
        className="w-full mb-6 py-2 px-4 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition cursor-pointer font-semibold"
        onClick={handleRegister}
      >
        Cadastrar Novo Usuário
      </button>
      <ul className="space-y-4">
        {paginatedUsers.map((user) => (
          <li
            key={user.id}
            className="p-4 bg-gray-100 rounded-lg shadow flex cursor-pointer font-semibold transition-all duration-200 hover:scale-105"
          >
            <div className="flex flex-col grow ">
              <span className="font-semibold text-lg">{user.name}</span>
              <span className="text-gray-600 text-sm">{user.email}</span>
            </div>
            <div className="flex items-center gap-2">
              <button className="bg-gray-300 p-2 rounded-2xl w-9 h-9 flex items-center justify-center transition cursor-pointer hover:scale-110">
                <Pencil className="text-blue-600" />
              </button>
              <button className="bg-gray-300 p-2 rounded-2xl w-9 h-9 flex items-center justify-center transition cursor-pointer hover:scale-110">
                <Trash className="text-blue-600" />
              </button>
            </div>
          </li>
        ))}
      </ul>
      <div className="flex justify-between items-center mt-6">
        <button
          className="px-3 py-1 bg-gray-300 rounded hover:bg-gray-400 disabled:opacity-50"
          onClick={handlePrev}
          disabled={page === 1}
        >
          Anterior
        </button>
        <span className="text-sm text-gray-700">
          Página {page} de {totalPages}
        </span>
        <button
          className="px-3 py-1 bg-gray-300 rounded hover:bg-gray-400 disabled:opacity-50"
          onClick={handleNext}
          disabled={page === totalPages}
        >
          Próxima
        </button>
      </div>
    </div>
  );
}