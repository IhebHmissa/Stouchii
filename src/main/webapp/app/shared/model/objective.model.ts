export interface IObjective {
  id?: string;
  name?: string | null;
  note?: string | null;
  userLogin?: string | null;
  amountTot?: number | null;
  amountVar?: number | null;
}

export const defaultValue: Readonly<IObjective> = {};
