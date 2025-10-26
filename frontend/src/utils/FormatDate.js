export const formatDate = (isoDate) => {
  const d = new Date(isoDate);

  const time = d
    .toLocaleTimeString("es-CO", {
      hour: "numeric",
      minute: "2-digit",
      hour12: true,
    })
    .toUpperCase();

  const date = `${d.getDate()}/${d.getMonth() + 1}/${d.getFullYear()}`;

  return `${date} - ${time}`;
};
