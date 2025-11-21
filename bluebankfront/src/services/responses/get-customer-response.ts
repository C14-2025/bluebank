import type { Customer } from "@/types/customer";
import type { Pageable } from "./paginated";

export interface GetCustomerResponse {
	content: Customer[];
	pageable: Pageable;
	totalElements: number;
	totalPages: number;
	last: boolean;
	size: number;
	number: number;
	numberOfElements: number;
}
