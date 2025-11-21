export function Hero() {
  return (
    <section className="relative overflow-hidden bg-linear-to-br from-primary/10 via-background to-background py-24 sm:py-32 bg-gray-200 h-screen">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="text-center">
          <h1 className="text-balance text-2xl font-bold tracking-tight text-foreground sm:text-2xl lg:text-5xl">
            Banco reinventado para a <span className="text-primary">era moderna</span>
          </h1>

          <p className="mx-auto mt-6 max-w-2xl text-pretty text-lg text-muted-foreground">
            Tenha uma experiência bancária segura, rápida e intuitiva. Gerencie suas finanças com confiança usando nossa plataforma moderna.
          </p>
        </div>
      </div>
    </section>
  )
}
