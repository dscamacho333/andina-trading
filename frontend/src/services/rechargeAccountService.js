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

  // const data = [
  //   {
  //     amount: 25.0,
  //     createdAt: "2025-04-28T09:15:30Z",
  //     status: "COMPLETED", // ó 'COMPLETADO'
  //   },
  //   {
  //     amount: 40.5,
  //     createdAt: "2025-04-29T13:22:10Z",
  //     status: "PENDING", // ó 'PENDIENTE'
  //   },
  //   {
  //     amount: 12.75,
  //     createdAt: "2025-04-30T18:47:05Z",
  //     status: "FAILED", // ó 'FALLIDO'
  //   },
  //   {
  //     amount: 100.0,
  //     createdAt: "2025-05-01T07:05:55Z",
  //     status: "COMPLETED",
  //   },
  //   {
  //     amount: 60.25,
  //     createdAt: "2025-05-01T15:30:00Z",
  //     status: "PENDING",
  //   },
  // ];

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
  const response = await fetch("http://localhost:8080/investor/api/account/recharge", {
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
