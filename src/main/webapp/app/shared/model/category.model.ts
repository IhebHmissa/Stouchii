export interface ICategory {
  id?: string;
  type?: string;
  nameCatego?: string | null;
  originType?: string;
  montant?: number | null;
  color?: string;
  userLogin?: string;
  minMontant?: number | null;
  maxMontant?: number | null;
  periodicty?: string | null;
}

export const defaultValue: Readonly<ICategory> = {};
