export const setCache = (key: string, data: unknown) => {
  sessionStorage.setItem(
    key,
    JSON.stringify({
      cacheTimestamp: new Date().getTime(),
      data: data
    })
  );
};

export const getCache = (key: string) => {
  const data = sessionStorage.getItem(key);
  return data ? JSON.parse(data) : null;
};
