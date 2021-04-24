import dayjs from 'dayjs';

export interface IPeriode {
  id?: string;
  dateDeb?: string | null;
  dateFin?: string | null;
  frequancy?: string | null;
  fixedMontant?: number | null;
  numberleft?: number | null;
  typeCatego?: string | null;
}

export const defaultValue: Readonly<IPeriode> = {};
