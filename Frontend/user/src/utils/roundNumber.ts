//Function 4.2 to 4.0
export const roundNumber = (number: number, decimalPlaces: number) => {
  const factor = Math.pow(10, decimalPlaces);
  return Math.round(number * factor) / factor;
};
