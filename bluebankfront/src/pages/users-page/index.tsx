import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router";
import { Trash } from "lucide-react"
import type { Customer } from "@/types/customer";
import { fetchCustomers, getCustomersListByDoc, searchCustomers } from "@/services/customers-services/get-customers-service";
import type { GetCustomerResponse } from "@/services/customers-services/responses/get-customer-response";
import { Funnel } from "lucide-react";
import { Menu, MenuItem } from "@mui/material";
import { deleteCustomer } from "@/services/customers-services/delete-customer-service";
import { toast } from "react-toastify";

export function UsersPage() {
  const [page, setPage] = useState<number>(0);
  const [users, setUsers] = useState<Customer[]>([]);
  const [totalPages, setTotalPages] = useState<number>(10);
  const [userNameFilter, setUserNameFilter] = useState<string>("");
  const [userDocFilter, setUserDocFilter] = useState<string>("");
  const [userDocTypeFilter, setUserDocTypeFilter] = useState<string>("");
  const [dobFilter, setDobFilter] = useState<string>("");
  const [nationalityFilter, setNationalityFilter] = useState<string>("");
  const [occupationFilter, setOccupationFilter] = useState<string>("");
  const [createdAtFilter, setCreatedAtFilter] = useState<string>("");
  const [updatedAtFilter, setUpdatedAtFilter] = useState<string>("");
  const [pageSizeFilter, setPageSizeFilter] = useState<string>("");
  const [currentSearchParams, setCurrentSearchParams] = useState<Record<string, string | number | undefined>>({});
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const menuAberto = Boolean(anchorEl);

  function handleClick(event: React.MouseEvent<HTMLElement>) {
    setAnchorEl(event.currentTarget);
  };

  function handleKeyDown(event: React.KeyboardEvent<HTMLInputElement>){
    if (event.key === "Enter") {
      event.preventDefault();
      handleApplyFilters({
        'doc-type': userDocTypeFilter,
        'doc-number': userDocFilter,
      });
    }
  };

  function handleDocTypeChange(e: React.ChangeEvent<HTMLSelectElement>) {
    setUserDocTypeFilter(e.target.value);
  }

  function handleClose() {
    setAnchorEl(null);
  };
  const paginatedUsers = users;
  const navigate = useNavigate();
  
  function handlePrev() {
    if(page > 0) setPage(page - 1);
  }

  function handleNext() {
    if (page < totalPages - 1) setPage(page + 1);
  }
  
  function handleRegisterCustomer() {
    navigate('/registercustomer');
  }

  function handleApplyFilters(overrides: Record<string, string | number | undefined> = {}) {
    const params: Record<string, string | number | undefined> = {
      dob: dobFilter || undefined,
      nationality: nationalityFilter || undefined,
      occupation: occupationFilter || undefined,
      createdAt: createdAtFilter || undefined,
      updatedAt: updatedAtFilter || undefined,
      pageSize: pageSizeFilter ? Number(pageSizeFilter) : undefined,
      'doc-type': userDocTypeFilter || undefined,
      'doc-number': userDocFilter || undefined,
    };

    const merged = { ...params, ...overrides };
    setCurrentSearchParams(merged);
    setPage(0);
    setAnchorEl(null);
  }

  useEffect(() => {
    async function fetchUsers() {
      try {
        if (currentSearchParams && Object.keys(currentSearchParams).length > 0) {
          const keys = Object.keys(currentSearchParams).filter(k => currentSearchParams[k] !== undefined && String(currentSearchParams[k]).trim() !== "");
          const hasDocType = keys.includes('doc-type');
          const hasDocNumber = keys.includes('doc-number');

          if (hasDocType && hasDocNumber && keys.length === 2) {
            const docType = String(currentSearchParams['doc-type']);
            const docNumber = String(currentSearchParams['doc-number']);
            const respRaw = (await getCustomersListByDoc(docNumber, docType))

            function isGetCustomerResponse(x: unknown): x is GetCustomerResponse {
              return typeof x === 'object' && x !== null && 'content' in (x as Record<string, unknown>);
            }

            if (isGetCustomerResponse(respRaw)) {
              const resp = respRaw;
              setUsers(resp.content ?? []);
              setTotalPages(resp.totalPages ?? (resp.content ? 1 : 0));
            } else if (Array.isArray(respRaw)) {
              const customers = respRaw as Customer[];
              setUsers(customers);
              setTotalPages(customers.length ? 1 : 0);
            } else if (respRaw) {
              setUsers([respRaw as Customer]);
              setTotalPages(1);
            } else {
              setUsers([]);
              setTotalPages(0);
            }
          } else {
            const resp = await searchCustomers({ ...currentSearchParams, page });
            setUsers(resp.content ?? []);
            setTotalPages(resp.totalPages ?? 0);
          }
        } else {
          const response = await fetchCustomers(page);
          setUsers(response.content ?? []);
          setTotalPages(response.totalPages ?? 0);
        }
      } catch (err) {
        setUsers([]);
        setTotalPages(0);
      }
    }
    fetchUsers();
  }, [page, currentSearchParams]);

  const filteredUsers = useMemo(() => {
    if (!userNameFilter) return paginatedUsers;
    return paginatedUsers.filter(user =>
      user.fullName.toLowerCase().includes(userNameFilter.toLowerCase())
    );
  }, [userNameFilter, paginatedUsers]);

  const handleDeleteUser = async (userId: string) => {
    const confirmDelete = window.confirm("Tem certeza que deseja deletar este Cliente?");
    if(!confirmDelete) return;

    try {
      await deleteCustomer(userId);
      setUsers(prev => prev.filter(u => u.id !== userId))
      toast.success("Cliente deletado com sucesso!", { type: "success" });
    } catch (error) {
      toast.error("Erro ao deletar Cliente", { type: "error" });
    }
    return;
  }

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-xl shadow-lg">
      <h2 className="text-center text-2xl font-bold mb-6">Lista de Clientes</h2>
      <div className="flex mb-4 flex-row gap-2 items-center justify-center">
        <button
        className="cursor-pointer"
        onClick={handleClick}
        >
          <Funnel />
        </button>
        <Menu
          anchorEl={anchorEl}
          open={menuAberto}
          onClose={handleClose}
          anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}
          transformOrigin={{ vertical: 'top', horizontal: 'right' }}
        >
          <MenuItem>
            <input
              className="mb-4 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Filtrar por nome"
              type="text"
              value={userNameFilter}
              onChange={(e) => setUserNameFilter(e.target.value)}
            />
          </MenuItem>

          <MenuItem>
            <div className="flex flex-row gap-2 w-full">
              <div className="w-1/3">
                <label className="block text-sm text-gray-700 font-semibold">Tipo de documento</label>
                <select
                  className="mb-4 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  value={userDocTypeFilter}
                  onChange={handleDocTypeChange}
                >
                  <option value="">Selecione</option>
                  <option value="CPF">CPF</option>
                  <option value="RG">RG</option>
                  <option value="Passaporte">Passaporte</option>
                </select>
              </div>
              <div className="flex-1">
                <label className="block text-sm text-gray-700 font-semibold">Número do documento</label>
                <input
                  className="mb-4 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  type="text"
                  value={userDocFilter}
                  onChange={(e) => setUserDocFilter(e.target.value)}
                  onKeyDown={handleKeyDown}
                  placeholder="Filtrar por documento"
                />
              </div>
            </div>
          </MenuItem>

          <MenuItem>
            <div className="grid grid-cols-2 gap-2 w-80">
              <div>
                <label className="block text-sm text-gray-700">Data de Nasc</label>
                <input type="date" value={dobFilter} onChange={(e) => setDobFilter(e.target.value)} className="w-full px-2 py-1 border rounded" />
              </div>
              <div>
                <label className="block text-sm text-gray-700">Nacionalidade</label>
                <input type="text" value={nationalityFilter} onChange={(e) => setNationalityFilter(e.target.value)} className="w-full px-2 py-1 border rounded" />
              </div>
              <div>
                <label className="block text-sm text-gray-700">Ocupação</label>
                <input type="text" value={occupationFilter} onChange={(e) => setOccupationFilter(e.target.value)} className="w-full px-2 py-1 border rounded" />
              </div>
              <div>
                <label className="block text-sm text-gray-700">Criado em</label>
                <input type="date" value={createdAtFilter} onChange={(e) => setCreatedAtFilter(e.target.value)} className="w-full px-2 py-1 border rounded" />
              </div>
              <div>
                <label className="block text-sm text-gray-700">Atualizado em</label>
                <input type="date" value={updatedAtFilter} onChange={(e) => setUpdatedAtFilter(e.target.value)} className="w-full px-2 py-1 border rounded" />
              </div>
              <div>
                <label className="block text-sm text-gray-700">Tamanho da página</label>
                <input type="number" min={1} value={pageSizeFilter} onChange={(e) => setPageSizeFilter(e.target.value)} className="w-full px-2 py-1 border rounded" />
              </div>
            </div>
          </MenuItem>

          <MenuItem className="flex gap-2">
            <button
              className="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700"
              onClick={() => handleApplyFilters()}
            >
              Aplicar filtros
            </button>
            <button
              className="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300"
              onClick={() => {
                setUserDocFilter("");
                setUserDocTypeFilter("");
                setDobFilter("");
                setNationalityFilter("");
                setOccupationFilter("");
                setCreatedAtFilter("");
                setUpdatedAtFilter("");
                setPageSizeFilter("");
                setCurrentSearchParams({});
                setAnchorEl(null);
              }}
            >
              Limpar
            </button>
          </MenuItem>
        </Menu>
        <button
          className="w-full py-2 px-4 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition cursor-pointer font-semibold"
          onClick={handleRegisterCustomer}
          >
          Cadastrar Novo Cliente
        </button>
      </div>
      <ul className="space-y-4">
        {filteredUsers.map((user) => (
          <li
            key={user.id}
            className="p-4 bg-gray-100 rounded-lg shadow flex cursor-pointer font-semibold transition-all duration-200 hover:scale-105"
            onClick={() => navigate(`/users/${user.id}`)}
          >
            <div className="flex flex-col grow ">
              <span className="font-semibold text-lg">{user.fullName}</span>
              <span className="text-gray-600 text-sm">{user.email}</span>
            </div>
            <div className="flex items-center gap-2">
              <button 
              className="bg-blue-600 w-10 h-10 p-2.5 rounded-lg flex items-center justify-center transition cursor-pointer hover:scale-110"
              type="button"
              value={user.id}
              onClick={(e) => { e.stopPropagation(); handleDeleteUser(user.id); }}>
                <Trash className="text-white" />
              </button>
            </div>
          </li>
        ))}
      </ul>
      <div className="flex justify-between items-center mt-6">
        <button
          className="bg-blue-600 rounded-lg flex items-center justify-center transition cursor-pointer hover:scale-110 px-3 py-1  hover:bg-blue-700 "
          onClick={handlePrev}
          disabled={page === 0}
        >
          <span className="text-white">
            Anterior
          </span>
        </button>
        <span className="text-sm">
          Página {page + 1} de {totalPages || 1}
        </span>
        <button
          className="bg-blue-600 rounded-lg flex items-center justify-center transition cursor-pointer hover:scale-110 px-3 py-1  hover:bg-blue-700 "
          onClick={handleNext}
          disabled={totalPages === 0 || page === totalPages - 1}
        >
          <span className="text-white">
            Próxima
          </span>
        </button>
      </div>
    </div>
  );
}