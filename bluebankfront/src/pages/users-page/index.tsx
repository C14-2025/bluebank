
import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router";
import { Pencil } from "lucide-react";
import { Trash } from "lucide-react"
import type { Customer } from "@/types/customer";
import { getCustomersList } from "@/services/get-customers-service";

const USERS_PER_PAGE = 4;

export function UsersPage() {
  const [page, setPage] = useState(0);
  const [users, setUsers] = useState<Customer[]>([]);

  const totalPages = Math.ceil(users.length / USERS_PER_PAGE);
  const paginatedUsers = useMemo(() => {
    return users.slice((page - 1) * USERS_PER_PAGE, page * USERS_PER_PAGE);
  }, [page, users]);
  const navigate = useNavigate();
  
  function handlePrev() {
    if(page > 0) setPage(page - 1);
  }

  function handleNext() {
    setPage(page + 1);
  }
  
  function handleRegister() {
    navigate('/register');
  }

  useEffect(() => {
    async function fetchUsers() {
      const response = await getCustomersList(page);
      setUsers(response);
    }
    fetchUsers();
  }, [page]);
  
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
              <span className="font-semibold text-lg">{user.fullName}</span>
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