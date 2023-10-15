export const buildJsonHeadersWithJwt = () => {
  const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;

  return new Headers({
    'Authorization': `Bearer ${localJwt}`,
    'Content-Type': 'application/json',
  });
}

export const buildFormDataHeadersWithJwt = () => {
  const localJwt: string = JSON.parse(localStorage.getItem("user") || "{}").jwt;

  return new Headers({
    'Authorization': `Bearer ${localJwt}`
  });
}
