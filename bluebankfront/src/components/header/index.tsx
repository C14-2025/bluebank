import { Link } from "react-router"

export function Header() {
  return (
    <header className="flex border-b border-b-gray-400 bg-card py-4 sm:px-6 mx-auto bg-blue-600">
      <div className="flex items-center gap-2 justify-between w-full">
        <span className="text-5xl font-bold text-foreground text-white cursor-default">BlueBank</span>
        <nav className="flex gap-2 align items-center flex-row">
          <Link to="/" className="cursor-pointer font-semibold text-blue-600 bg-white px-3 py-2 rounded-xl transition-all duration-200 hover:scale-105 hover:bg-blue-800 hover:text-white"> Home </Link>
          <Link to="users" className="cursor-pointer font-semibold text-blue-600 bg-white px-3 py-2 rounded-xl transition-all duration-200 hover:scale-105 hover:bg-blue-800 hover:text-white"> Clientes </Link>
          <Link to="transactionsaccounts" className="cursor-pointer font-semibold text-blue-600 bg-white px-3 py-2 rounded-xl transition-all duration-200 hover:scale-105 hover:bg-blue-800 hover:text-white"> Transações </Link>
        </nav>
      </div>
    </header>
  )
}
