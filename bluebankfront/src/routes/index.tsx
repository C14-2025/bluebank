import { Header } from "@/components/header";
import { HomePage } from "@/pages/home";
import { TransactionsPage } from "@/pages/transactions";
import { UserProfile } from "@/pages/user-details-page";
import { RegisterPage } from "@/pages/user-register";
import { UsersPage } from "@/pages/users-page";
import { Routes, Route, Navigate } from "react-router";

export function AppRoutes() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="register" element={<RegisterPage />} />
        <Route path="users" element={<UsersPage />} />
        <Route path="transactions" element={<TransactionsPage />} />
        <Route path="users/:id" element={<UserProfile />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </>
  );
}
