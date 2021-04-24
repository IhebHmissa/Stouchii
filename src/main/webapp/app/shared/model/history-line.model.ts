import dayjs from 'dayjs';

export interface IHistoryLine {
  id?: string;
  categoryName?: string;
  dateModif?: string;
  montant?: number;
  userLogin?: string | null;
  note?: string | null;
  typeCatego?: string | null;
}

export const defaultValue: Readonly<IHistoryLine> = {};
