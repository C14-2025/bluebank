import { useNavigate } from "react-router-dom";
import type { InferType } from "yup";
import { Formik, Form, Field, ErrorMessage } from "formik";

import { createCustomer } from "@/services/put-customers-service";
import { registerSchema } from "@/validators/register-form-validator";

import { maskCountryCode, maskCPF, maskPhone } from "@/utils/masks";

type RegisterFormValues = InferType<typeof registerSchema>;

export function RegisterPage() {
  const navigate = useNavigate();

  const handleSubmitForm = async (values: RegisterFormValues) => {
    const ok = await createCustomer(values);
    if(!ok) return;
    navigate("/users");
  }
  
  return (
    <div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-xl shadow-lg">
      <h2 className="text-center text-2xl font-bold mb-6 text-blue-700">Registro de Cliente</h2>

      <Formik<RegisterFormValues>
        initialValues={{
          fullName: "",
          dob: "",
          nationality: "",
          countryCode: "",
          phone: "",
          email: "",
          occupation: "",
          docType: "",
          docNumber: "",
        }}
        validationSchema={registerSchema}
        onSubmit={handleSubmitForm}>
        {({ values, setFieldValue }) => (
          <Form className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Nome completo</label>
              <Field name="fullName" type="text" className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
              <ErrorMessage name="fullName" component="div" className="text-red-500 text-xs" />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Data de nascimento</label>
              <Field name="dob" type="date" className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
              <ErrorMessage name="dob" component="div" className="text-red-500 text-xs" />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Nacionalidade</label>
              <Field name="nationality" type="text" className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
              <ErrorMessage name="nationality" component="div" className="text-red-500 text-xs" />
            </div>

            <div className="flex gap-2">
              <div className="w-1/3">
                <label className="block text-sm font-medium text-gray-700">Código do país</label>
                <input
                  name="countryCode"
                  value={values.countryCode}
                  onChange={(e) => setFieldValue("countryCode", maskCountryCode(e.target.value))}
                  className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <ErrorMessage name="countryCode" component="div" className="text-red-500 text-xs" />
              </div>

              <div className="w-2/3">
                <label className="block text-sm font-medium text-gray-700">Telefone</label>
                <input
                  name="phone"
                  value={values.phone}
                  onChange={(e) => setFieldValue("phone", maskPhone(e.target.value))}
                  className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="(99) 99999-9999"
                />
                <ErrorMessage name="phone" component="div" className="text-red-500 text-xs" />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Email</label>
              <Field name="email" type="email" className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
              <ErrorMessage name="email" component="div" className="text-red-500 text-xs" />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Ocupação</label>
              <Field name="occupation" type="text" className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
              <ErrorMessage name="occupation" component="div" className="text-red-500 text-xs" />
            </div>

            <div className="flex gap-2">
              <div className="w-1/2">
                <label className="block text-sm font-medium text-gray-700">Tipo de documento</label>
                <Field as="select" name="docType" className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                  <option value="">Selecione</option>
                  <option value="CPF">CPF</option>
                  <option value="RG">RG</option>
                  <option value="Passaporte">Passaporte</option>
                </Field>
                <ErrorMessage name="docType" component="div" className="text-red-500 text-xs" />
              </div>

              <div className="w-1/2">
                <label className="block text-sm font-medium text-gray-700">Número do documento</label>
                <div>
                  {values.docType === "CPF" ? (
                    <input
                      name="docNumber"
                      value={values.docNumber}
                      onChange={(e) => setFieldValue("docNumber", maskCPF(e.target.value))}
                      className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="000.000.000-00"
                      maxLength={14}
                    />
                  ) : (
                    <input
                      name="docNumber"
                      value={values.docNumber}
                      onChange={(e) => setFieldValue("docNumber", e.target.value)}
                      type="text"
                      className="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder={values.docType === "RG" ? "RG" : "Passaporte"}
                      maxLength={20}
                    />
                  )}
                </div>
                <ErrorMessage name="docNumber" component="div" className="text-red-500 text-xs" />
              </div>
            </div>

            <button type="submit" className="w-full py-2 px-4 bg-blue-600 text-white rounded-lg font-semibold hover:bg-blue-700 transition">Registrar</button>
          </Form>
        )}
      </Formik>
    </div>
  );
}
