export function sanitize(values: Record<string, any>) {
  const out = { ...values };
  if (out.countryCode) {
    const cleaned = String(out.countryCode).replace(/[^\d+]/g, "");
    const digitsOnly = cleaned.replace(/\+/g, "");
    out.countryCode = digitsOnly ? `+${digitsOnly}` : "";
  }
  if (out.phone) out.phone = out.phone.replace(/\D/g, "");
  if (out.docType === "CPF" && out.docNumber) out.docNumber = out.docNumber.replace(/\D/g, "");
  if (out.email) out.email = String(out.email).trim().toLowerCase();
  return out;
}