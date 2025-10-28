export const validateUserBankRelation = async (userId) => {
  const response = await fetch(
    `http://localhost:8080/api/investor/account/bank/validate/${userId}`
  );
  const data = await response.json();
  return data;

  // return { status: false, details: "No hay relación de cuenta bancaria" };
};

export const getRechargeHistory = async (userId) => {
  const response = await fetch(
    `http://localhost:8080/api/investor/account/recharges/${userId}`
  );
  const data = await response.json();


  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(
      errorData.message || "Error al obtener el historial de recargas"
    );
  }

  return data;
};

export const validatePendingRecharges = async (userId) => {
  await fetch(
    `http://localhost:8080/api/investor/account/recharges/pending/check/${userId}`
  );
};

export const relateBankAccount = async (data) => {
  const response = await fetch(
    "http://localhost:8080/api/investor/account/bank/register",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }
  );

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(
      errorData.message || "Error al relacionar la cuenta bancaria"
    );
  }
};

export const rechargeAccount = async (data) => {
  const response = await fetch("http://localhost:8080/api/investor/account/recharge", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(
      errorData.message || "Error al realizar la recarga de cuenta"
    );
  }
};
