export function sanitize(values: Record<string, unknown>) {
	const out: Record<string, unknown> = { ...values };
	if (out.countryCode) {
		const cleaned = String(out.countryCode).replace(/[^\d+]/g, "");
		const digitsOnly = cleaned.replace(/\+/g, "");
		out.countryCode = digitsOnly ? `+${digitsOnly}` : "";
	}
	if (out.phone) out.phone = String(out.phone).replace(/\D/g, "");
	if (out.docType === "CPF" && out.docNumber)
		out.docNumber = String(out.docNumber).replace(/\D/g, "");
	if (out.email) out.email = String(out.email).trim().toLowerCase();
	return out;
}
