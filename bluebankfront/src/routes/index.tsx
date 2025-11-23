import { Bounce, ToastContainer } from "react-toastify";

import { Header } from "@/components/header";
import { RegisterAccountPage } from "@/pages/account-register";
import { HomePage } from "@/pages/home";
import { TransactionsPage } from "@/pages/transactions";
import { UserProfile } from "@/pages/user-details-page";
import { RegisterCustomerPage } from "@/pages/user-register";
import { UsersPage } from "@/pages/users-page";
import { Routes, Route, Navigate } from "react-router";

export function AppRoutes() {
	return (
		<>
			<Header />
			<Routes>
				<Route path="/" element={<HomePage />} />
				<Route path="registercustomer" element={<RegisterCustomerPage />} />
				<Route
					path="registeraccount/:customerId"
					element={<RegisterAccountPage />}
				/>
				<Route path="users" element={<UsersPage />} />
				<Route path="transactionsaccounts" element={<TransactionsPage />} />
				<Route path="users/:id" element={<UserProfile />} />
				<Route path="*" element={<Navigate to="/" replace />} />
			</Routes>

			<ToastContainer
				position="top-center"
				autoClose={5000}
				hideProgressBar={false}
				newestOnTop={false}
				closeOnClick={false}
				rtl={false}
				pauseOnFocusLoss
				draggable
				pauseOnHover
				theme="colored"
				transition={Bounce}
			/>
		</>
	);
}
