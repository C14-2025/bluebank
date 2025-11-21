import { Hero } from "@/components/hero";
import { Link } from "react-router";

export function HomePage() {
  return (
    <div className="flex flex-col h-screen bg-gray-100 m-0 p-0">
      <main className="max-w-3xl mx-auto px-4 py-16 text-center my-20">
        <h1 className="text-balance text-4xl font-bold tracking-tight text-foreground sm:text-5xl lg:text-6xl">Bem-vindo ao BlueBank</h1>
        <p className="mx-auto mt-5 max-w-2xl text-pretty text-lg text-muted-foreground">Um banco simples — crie contas e veja usuários.</p>

        <div className="flex items-center justify-center gap-4 mt-6">
          <Link to="register" className="px-5 py-2 rounded bg-blue-600 text-white text-sm">Criar conta</Link>
          <Link to="users" className="px-5 py-2 rounded border bg-gray-200 border-gray-200 text-sm text-gray-700 font-semibold">Ver usuários</Link>
        </div>
      </main>

      <Hero />
    </div>
  )
}