import { useEffect, useState } from "react";
import { validateUserBankRelation } from "../../services/rechargeAccountService";
import { getUserFromToken } from "../../services/authService";
import DashboardLayout from "../../layouts/DashboardLayout";
import { Loading } from "../../components/Loading/Loading";
import { RelateBankAccount } from "../..//components/RelateBankAccount/RelateBankAccount";
import { Recharge } from "../../components/Recharge/Recharge";
import { ValidateRelateBankAccount } from "../..//components/ValidateRelateBankAccount/ValidateRelateBankAccount";

export const RechargeAccount = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [user, setUser] = useState(null);
  const [bankRelationStatus, setBankRelationStatus] = useState(null);
  const [bankRelationDetail, setBankRelationDetail] = useState("");

  useEffect(() => {
    setIsLoading(true);

    const user = getUserFromToken();

    if (!user) {
      return;
    }

    setUser(user);

    validateUserBankRelation(user.userId)
      .then((data) => {
        setBankRelationStatus(data.status);
        setBankRelationDetail(data.details);
      })
      .catch((error) => {
        console.error(
          "Respuesta inesperada al validar el estado de cuenta: " + error
        );
      })
      .finally(() => setIsLoading(false));
  }, [validateUserBankRelation]);

  if (isLoading) {
    return (
      <DashboardLayout>
        <Loading message={"Cargando estado de cuenta bancaria..."} />
      </DashboardLayout>
    );
  }

  if (bankRelationStatus === null) {
    return (
      <DashboardLayout>
        <RelateBankAccount user={user} />
      </DashboardLayout>
    );
  }

  if (bankRelationStatus === false) {
    return (
      <DashboardLayout>
        <ValidateRelateBankAccount bankRelationDetail={bankRelationDetail} />
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <Recharge user={user} />
    </DashboardLayout>
  );
};
