import dayjs from 'dayjs';

export interface INotification {
  id?: string;
  amount?: number | null;
  userLogin?: string | null;
  categoryName?: string | null;
  time?: string | null;
  type?: string | null;
}

export const defaultValue: Readonly<INotification> = {};
