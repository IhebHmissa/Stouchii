import { IUser } from 'app/shared/model/user.model';

export interface IApplicationUser {
  id?: string;
  soldeUser?: number | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IApplicationUser> = {};
